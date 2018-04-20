package com.tv.finance.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.google.firebase.cloud.FirestoreClient;
import com.tv.finance.data.TxnParser;
import com.tv.finance.model.Ticker;
import com.tv.finance.model.Txn;



@SpringBootApplication
public class FinanceClientApplication {

	private static Logger log = LoggerFactory.getLogger(FinanceClientApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(FinanceClientApplication.class, args);
		try {
			new FinanceClientApplication().writeTxns("2018.csv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Stream<Txn> parseAndStreamTxns(String fileName) throws Exception {
		try {
			return TxnParser.fromStream(FinanceClientApplication.class.getClassLoader().getResourceAsStream(fileName))
				.stream();
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Exception Parsing and streaming Txns", e);

		}
		
	}
	private void writeTickers(String file) throws Exception {
		try {
			Firestore fs =  FirestoreClient.getFirestore();
			WriteBatch b = fs.batch();
			

			CollectionReference cr = fs.collection("ticker");
			Stream<Txn> txns = this.parseAndStreamTxns(file);
			if(txns ==null) {
				return;
			}
			Map<String,Ticker> folio = new HashMap<String,Ticker>();
			txns
					.map(txn -> new Ticker(txn.getCode().toUpperCase(),txn.getName()))
					.forEach(t -> {
						if(!folio.containsKey(t.getCode())) {
							folio.put(t.getCode(), t);
						}
					});
			
			folio.keySet().forEach(code -> {
									
						b.create(cr.document(), folio.get(code));
					
				});
			b.commit()
				.get()
				.stream()
				.forEach(wr ->{
					log.debug("write >"+wr.getUpdateTime());
			});;
		} catch (InterruptedException |ExecutionException e ) {
			log.error("Exception", e);
		} 
		
		
		
	
	}
	private void writeTxns(String file) {
		try {
			Firestore fs =  FirestoreClient.getFirestore();
			WriteBatch b = fs.batch();
			
				//2018
			CollectionReference cr = fs.collection("portfolio/BrEnVJguni3T3dDLVSc0/txns");
			
			this.parseAndStreamTxns(file)
				.forEach(t -> {
									
						b.create(cr.document(), t);
					
				});
			b.commit()
				.get()
				.stream()
				.forEach(wr ->{
					log.debug("write >"+wr.getUpdateTime());
			});;
		} catch (Exception e ) {
			log.error("Exception", e);
		} 
		
		
		
	
	}
	private void readTxns() {
		try {
			Firestore fs =  FirestoreClient.getFirestore();
	

			QuerySnapshot qs = fs.collection("portfolio/yhRy4aLy9x3ShA5bbUBW/txns").get().get(5, TimeUnit.SECONDS);
			Iterator<QueryDocumentSnapshot> i = qs.getDocuments().iterator();
			QueryDocumentSnapshot qds =null;
			while(i.hasNext()) {
				qds = i.next();
				log.info("value-> id:"+qds.getId());
				//log.info("value-> name:"+qds.getString("name"));	
			}
		} catch (InterruptedException |ExecutionException|TimeoutException e ) {
			log.error("Exception", e);
		} 
		
		
		
	
	}
	private void readTickers() {
		try {
			Firestore fs =  FirestoreClient.getFirestore();
			QuerySnapshot qs = fs.collection("ticker").get().get(5, TimeUnit.SECONDS);
			Iterator<QueryDocumentSnapshot> i = qs.getDocuments().iterator();
			QueryDocumentSnapshot qds =null;
			while(i.hasNext()) {
				qds = i.next();
				log.info("value-> code:"+qds.getString("code"));
				log.info("value-> name:"+qds.getString("name"));	
			}
		} catch (InterruptedException |ExecutionException|TimeoutException e ) {
			log.error("Exception", e);
		} 
		
		
		
	
	}
}
