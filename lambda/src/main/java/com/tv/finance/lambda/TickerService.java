package com.tv.finance.lambda;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class TickerService {
	
	private static final Logger logger = LogManager.getLogger(TickerService.class);
	
	/**
	 * Name of the ticker collection
	 */
	private static final String TICKER ="ticker";
	
	/**
	 * Code field
	 */
	private static final String CODE ="code";
	
	public static Stream<String> getTickerCodes() {
		
		Firestore fs;
		QuerySnapshot qs;


		try {

			fs =  FinanceClient.getFirestore();

			qs = fs.collection(TICKER)
					.select(CODE)
					.orderBy(CODE)
					.get().get(30, TimeUnit.SECONDS);
			
			return qs.getDocuments()
				.stream()
				.map(qds -> qds.getString(CODE));
								
		} catch (InterruptedException |ExecutionException|TimeoutException e ) {
			logger.error("Exception in readTickers ", e);
		}

		return null;
	}

}
