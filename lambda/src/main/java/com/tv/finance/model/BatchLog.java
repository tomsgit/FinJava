package com.tv.finance.model;

import java.util.Date;

public class BatchLog {
	
	private String status;
	private Date date;
	
	public BatchLog(String pStatus) {
		this.status= pStatus;
		this.date= new Date();
	}
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}



	

}
