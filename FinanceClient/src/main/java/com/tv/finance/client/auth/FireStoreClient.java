package com.tv.finance.client.auth;

import org.springframework.stereotype.Component;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Component
public class FireStoreClient {
	

	
	private Firestore client(){
		
		return FirestoreClient.getFirestore();
	}

}
