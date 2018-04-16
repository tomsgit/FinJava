package com.tv.finance.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.access.InvalidInvocationException;

import com.tv.finance.client.FinanceClientApplication;
import com.tv.finance.model.Txn;

public class TxnParser {
	
	private CSVParser parser;
	
	private static Logger log = LoggerFactory.getLogger(FinanceClientApplication.class);
	
	public static TxnParser fromFile(File file) throws IOException{
		TxnParser ins = new TxnParser();
		ins.parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.EXCEL);
		return ins;
	}
	
	public static TxnParser fromStream(InputStream stream) throws IOException{
		TxnParser ins = new TxnParser();
		ins.parser = CSVParser.parse(stream, Charset.defaultCharset(), CSVFormat.EXCEL);
		return ins;
	}
	
	public Stream<Txn> stream() throws IOException{
		if(parser ==null) {
			throw new InvalidInvocationException("parser is null, call initialisation method first");
			
		}
		return parser.getRecords()
					.stream()
					.filter(r -> r.getRecordNumber()>1)
					.filter(r -> !EXLCUDE.equalsIgnoreCase(r.get(10)))
					.map(this::csvToTxn);
	}

	private Txn csvToTxn(CSVRecord r) {
		
		
		if(r ==null) {
			return null;	
		}
		log.info("csvToTxn"+r.getRecordNumber()+" >>>"+r.get(3));
		Txn t =new Txn();
		
		//0Date	1Time	2Exchange	3Script	4Type	5Shares	6Price	7Commission	8Name	9Broker	10Exclude	11Remarks
		
		t.setDate(getDateTime(r.get(0),r.get(1)));
		t.setExchange(NSE.equalsIgnoreCase(r.get(2))?1:0);
		t.setCode(r.get(3));
		t.setType(BUY.equalsIgnoreCase(r.get(4))?0:1);
		t.setShares(Double.valueOf(r.get(5)).intValue());
		t.setPrice(Double.parseDouble(r.get(6)));
		t.setCommission(Double.parseDouble(r.get(7)));
		t.setName(r.get(8));
		t.setExchange(ICICI.equalsIgnoreCase(r.get(9))?1:0);
		t.setRemarks(r.get(11));
		return t;
			
	}
	private static String TIME_SEPERATOR=" ";
	private static String NSE="NSE";
	private static String ICICI="0";
	private static String BUY="BUY";
	private static String EXLCUDE="1";
	private static SimpleDateFormat FMT =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date getDateTime(String dt, String time) {
		
		String date=null;
		try {
			date =new StringBuilder(dt).append(TIME_SEPERATOR).append(time).toString();
			return FMT.parse(date);
		} catch (ParseException e) {
			log.error("Parse exception with "+date,e);;
			return null;
		}
		
	}
}
