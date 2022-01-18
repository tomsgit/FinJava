package com.tv.finance.lambda;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tv.finance.model.Quote;


public class App implements RequestHandler<Input, String>{
	static final Logger logger = LogManager.getLogger(App.class);


	public static void main(String[] args) {
		App app = new App();
		Input in = new Input();
		in.setDate("18-01-2022");
		app.handleRequest(in, null);
	}
	
	public String handleRequest(Input input, Context context) {
		
		Map<String, String> tickersToTrack; 
		Stream<Quote> quotesToPersist;
		String status="SUCCESS";
		
		String in = null;
		try {
			
			tickersToTrack = TickerService.getTickerCodes()
				.peek(code -> logger.info("value-> code:{}",code))
				.collect(Collectors.toMap(code -> code, code -> code));
			
			in = (input == null)?null:input.getDate();
			logger.debug("input is {}",in);
			
			quotesToPersist = QuoteService.filterAndStreamQuotes(tickersToTrack::containsKey,in)
				.peek(q -> logger.debug("quote for {}",q.getCode()));

				
			QuoteService.persist(quotesToPersist);
			Thread.sleep(2000);
			
			/*logger.debug("-------------LATEST---------------------------------------------------------------");
			
			QuoteService.getAllLatestFromFS()
				.forEach(map -> logger.debug("Latest code :{} Date:{}",map.get("code") ,map.get("date")));
			logger.debug("-------------ALL---------------------------------------------------------------");
			QuoteService.getAllQuotesFromFS()
			.forEach(map -> logger.debug("ALL code :{} Date:{}",map.get("code") ,map.get("date")));
			*/
			
			
		} catch (Exception e) {
			status =  e.getMessage();
		}finally {
			BatchLogService.logIt(status);
		}
		return "Completing  "+status;
	}


	

}
