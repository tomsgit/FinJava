package com.tv.finance.lambda;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class FinanceClient {
	
	private GoogleCredentials credentials = null;
	
	private static final Logger logger = LogManager.getLogger(FinanceClient.class);
	
	private static final String AUTH_FILE="Finance-5e405c3c73fa.json";
	
	private static final String PROJECT_ID="finance-46385";
	
	private static  FinanceClient ins=null;
	private Firestore firestore;
	private FinanceClient() {
		
		
	}
	private static synchronized FinanceClient init() {
		
		if(ins==null) {
			logger.debug("creating new instance");
			ins= new FinanceClient();
			ins.setCredentials();
			logger.debug("Credentials set");
			/*FirebaseOptions options = new FirebaseOptions.Builder()
				    .setCredentials(ins.getCredentials())
				    .build();*/
			ins.firestore = FirestoreOptions.newBuilder()
						        .setCredentials(ins.getCredentials())
						        .setProjectId(PROJECT_ID)
						        .build()
						        .getService();

			logger.debug("FirebaseApp inited");
		}
		logger.debug("FinanceClient inited");
		return ins;
	}
	public static Firestore getFirestore() {
		return FinanceClient.init().firestore;
	}
	public GoogleCredentials getCredentials() {
		return credentials;
	}
	private GoogleCredentials setCredentials() {
		
		InputStream serviceAccount;

		try {
			serviceAccount =this.getClass().getClassLoader().getResourceAsStream(AUTH_FILE);
			credentials = GoogleCredentials.fromStream(serviceAccount);
		} catch (IOException e ) {
			logger.error("exception getting credentials",e);
		} 
		
		return credentials;
	}

}
