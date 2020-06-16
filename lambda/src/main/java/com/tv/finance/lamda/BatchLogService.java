package com.tv.finance.lamda;

import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import com.tv.finance.model.BatchLog;

public class BatchLogService {
	
	private static final String BATCH_LOG ="BATCHLOG";
	static final Logger logger = LogManager.getLogger(BatchLogService.class);
	public static void logIt(String status) {
		
		Firestore fs;
		WriteBatch fsBatch;
		CollectionReference batchLog;
		
		
		
		try {
			
			fs =  FinanceClient.getFirestore();
			fsBatch = fs.batch();
			
			batchLog = fs.collection(BATCH_LOG);
			
			BatchLog log = new BatchLog(status);
			
			fsBatch.create(batchLog.document(), log);
			
			fsBatch.commit()
			.get()
			.stream()
			.forEach(wr -> logger.debug("write >"+wr.getUpdateTime()));
		} catch (InterruptedException e) {
			logger.error("Exception logging the batch",e);
		} catch (ExecutionException e) {
			logger.error("Exception logging the batch",e);
		}
		
		
	}

}
