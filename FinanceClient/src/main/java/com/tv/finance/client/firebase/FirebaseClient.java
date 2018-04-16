package com.tv.finance.client.firebase;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tv.finance.client.auth.CredentialsHolder;

@Component
public class FirebaseClient implements InitializingBean{
	
	@Autowired
	private CredentialsHolder credentialsHolder;
	
	private FirebaseApp client(){
		
		return FirebaseApp.getInstance();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		FirebaseOptions options = new FirebaseOptions.Builder()
			    .setCredentials(credentialsHolder.credentials())
			    .build();
		FirebaseApp.initializeApp(options);
		
	}

}
