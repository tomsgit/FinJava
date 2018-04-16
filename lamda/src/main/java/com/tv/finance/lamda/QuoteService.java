package com.tv.finance.lamda;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.tv.finance.model.Quote;

public class QuoteService {
	
	
	private static final Logger logger = LogManager.getLogger(QuoteService.class);
	private static SimpleDateFormat DATE_FORMAT =new SimpleDateFormat("dd-MMM-yy");
	
	//private static final String NSE_BHAV_COPY = "https://www.nseindia.com/content/historical/EQUITIES/2018/APR/cm10APR2018bhav.csv.zip";
	
	private static final String NSE_BHAV_COPY = "https://www.nseindia.com/content/historical/EQUITIES/{year}/{month}/cm{date}bhav.csv.zip";
	
	/**
	 * Name of the Quote collection
	 */
	private static final String QUOTE ="quote";
	/**
	 * Name of the Quote collection
	 */
	private static final String LATEST_QUOTE ="latestquote";

	private static final Locale LOCALE = new Locale("en","IN");
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy",LOCALE);
	
	public static CSVParser parser(InputStream stream) throws IOException{
		return  CSVParser.parse(stream, Charset.defaultCharset(), CSVFormat.EXCEL);
	}
	
	public static Stream<Quote> streamAll() throws Exception{
		
	    return QuoteService.getQuotesForDateString(null)
					.map(QuoteService::csvToQuote);
					//.forEach(q -> logger.debug("quote {},{},{},{},{},{},{},{},{},", q.getCode(), q.getHigh(), q.getLow(), q.getClose(), q.getLast(), q.getDate(), q.getPrevclose()));
	}

	private static Stream<CSVRecord> getQuotesForDate( LocalDate ldt) throws Exception{
		InputStream is=null;
		ZipInputStream zis;
		
		if(ldt.getDayOfWeek() == DayOfWeek.SATURDAY || ldt.getDayOfWeek() == DayOfWeek.SUNDAY) {
			logger.error("Skipping on weekends");
			throw new Exception("Skipping on weekends");
		}
		String url =getUrl(ldt);
		logger.info("Fetching from :{}",url);
		is = HttpService.getStream(url);
		
		if(is ==null) {
			throw new Exception("Unable to get file from HttpService");
		}
		
		zis = new ZipInputStream(is);		
		zis.getNextEntry();

	    return StreamSupport.stream(QuoteService.parser(zis).spliterator(),false)
					.filter(r -> r.getRecordNumber()>1);
	}
	
	private static Stream<CSVRecord> getQuotesForDateString(String date) throws Exception{
		LocalDate ldt;
		if(date == null) {
			ldt = LocalDate.now();
		}else {
			ldt = LocalDate.from(DATE_FORMATTER.parse(date));
		}
		return getQuotesForDate(ldt);
	}
	
	private static String getUrl(LocalDate dt) {
		
		String year = String.valueOf(dt.getYear());
		String month = dt.format(DateTimeFormatter.ofPattern("MMM",LOCALE)).toUpperCase();		
		String date = dt.format(DateTimeFormatter.ofPattern("ddMMMyyyy",LOCALE)).toUpperCase();
		
		logger.debug("year {}",year);
		logger.debug("month {}",month);
		logger.debug("date {}",date);

		return NSE_BHAV_COPY.replace("{year}", year).replace("{month}", month).replace("{date}", date);

	}
	
	private static Quote csvToQuote(CSVRecord r) {		
		
		if(r ==null) {
			return null;	
		}
		
		logger.debug("csvToQuote"+r.getRecordNumber());
		Quote q =new Quote();
		
		//0SYMBOL	1SERIES	2OPEN	3HIGH	4LOW	5CLOSE	6LAST	7PREVCLOSE	8TOTTRDQTY	9TOTTRDVAL	10TIMESTAMP	11TOTALTRADES	12ISIN

		q.setCode(r.get(0));		
		
		q.setOpen(Double.parseDouble(r.get(2)));
		q.setHigh(Double.parseDouble(r.get(3)));
		q.setLow(Double.parseDouble(r.get(4)));
		
		q.setClose(Double.parseDouble(r.get(5)));
		q.setLast(Double.parseDouble(r.get(6)));
		q.setPrevclose(Double.parseDouble(r.get(7)));
		
		q.setTradeQty(Integer.parseInt(r.get(8)));
		q.setTradeValue(Double.parseDouble(r.get(9)));		
		q.setDate(getDate(r.get(10)));
		
		q.setTotTrades(Integer.parseInt(r.get(11)));
		q.setIsin(r.get(12));
		
		return q;
			
	}

	
	
	private static Date getDate(String dt) {
				
		try {
			synchronized (DATE_FORMAT) {
				return DATE_FORMAT.parse(dt);
			}
			
		} catch (ParseException e) {
			logger.error("Parse exception with "+dt,e);
			return null;
		}
		
	}
	public static void persist(Collection<Quote> quotesToPersist) {
		QuoteService.persist(quotesToPersist.stream());		
	}
	
	public static void persist(Stream<Quote> quotesToPersist) {
		
		Firestore fs;
		WriteBatch b;
		CollectionReference quote;
		CollectionReference latestquote;
		
		try {
			
			if(quotesToPersist ==null) {
				logger.error("Nothing to persist");
				return;
			}
			fs =  FinanceClient.getFirestore();
			b = fs.batch();
			
			quote = fs.collection(QUOTE);
			latestquote = fs.collection(LATEST_QUOTE);
			
			
			StreamSupport.stream(latestquote.get().get().spliterator(), false)
				.forEach(d -> b.delete(d.getReference()));

			quotesToPersist
				.forEach(q -> {
					b.create(quote.document(), q);
					b.create(latestquote.document(), q);

				});

			b.commit()
				.get()
				.stream()
				.forEach(wr -> logger.debug("write >"+wr.getUpdateTime()));
			
		} catch (InterruptedException |ExecutionException e ) {
			logger.error("Exception", e);
		} 
		
	}
	
	public static Stream<Map<String,Object>> getAllLatestFromFS(){
		Spliterator<QueryDocumentSnapshot> s;
		try {
			s = FinanceClient.getFirestore().collection(LATEST_QUOTE)
					.orderBy("code")
					.get()
					.get(30000, TimeUnit.SECONDS)
					.spliterator();
			
			return StreamSupport.stream(s, false)
					.map(qds -> qds.getData());
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			logger.error("Exception getting lates quotes from FS",e);
		}
		return null;
	}
	public static Stream<Map<String,Object>> getAllQuotesFromFS(){
		Spliterator<QueryDocumentSnapshot> s;
		try {
			s = FinanceClient.getFirestore().collection(QUOTE)
					.orderBy("code")
					.get()
					.get(30000, TimeUnit.SECONDS)
					.spliterator();
			
			return StreamSupport.stream(s, false)
					.map(qds -> qds.getData());
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			logger.error("Exception getting quotes from FS",e);
		}
		return null;
	}
	
	public static Stream<Quote> filterAndStreamQuotes(Predicate<String> predicate, String date) throws Exception {
		
		return  QuoteService.getQuotesForDateString(date)
				.filter(record -> predicate.test(record.get(0)))
				.map(QuoteService::csvToQuote);
	}
}
