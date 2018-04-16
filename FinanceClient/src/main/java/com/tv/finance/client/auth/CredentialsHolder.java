package com.tv.finance.client.auth;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;

@Component
public class CredentialsHolder {

	
	private static final String AUTH_FILE="Finance-5e405c3c73fa.json";
	//private static final String AUTH_FILE="Finance-7307f6a855c3.json";
	
	
	private GoogleCredentials credentials;
	
	private Logger logger = LoggerFactory.getLogger(CredentialsHolder.class);
	
	public GoogleCredentials credentials() {
		InputStream serviceAccount;
		
		if(credentials != null) {
			return credentials;
		}
		try {
			serviceAccount =this.getClass().getClassLoader().getResourceAsStream(AUTH_FILE);
			credentials = GoogleCredentials.fromStream(serviceAccount);
		} catch (IOException e ) {
			logger.error("exception getting credentials",e);
		} 
		
		return credentials;
	}
	
}
