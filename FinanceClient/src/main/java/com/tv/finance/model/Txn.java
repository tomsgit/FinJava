package com.tv.finance.model;

import java.util.Date;

public class Txn {
	
	private String code;
	
	private String name;
	//
	private int type;
	
	private int broker;
	
	private int exchange;
	
	private int shares;
	
	private double price;
	
	private double commission;
	
	private double value;
	
	private String remarks;
	
	private Date date;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBroker() {
		return broker;
	}

	public void setBroker(int broker) {
		this.broker = broker;
	}

	public int getExchange() {
		return exchange;
	}

	public void setExchange(int exchange) {
		this.exchange = exchange;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		String kvsep = "-";
		String fieldSep="< | >";
		
		sb.append("TXN").append("->");
		
		sb.append("code").append(kvsep).append(code).append(fieldSep);
		sb.append("name").append(kvsep).append(name).append(fieldSep);
		sb.append("type").append(kvsep).append(type).append(fieldSep);
		sb.append("broker").append(kvsep).append(broker).append(fieldSep);
		sb.append("exchange").append(kvsep).append(exchange).append(fieldSep);
		
		sb.append("shares").append(kvsep).append(shares).append(fieldSep);
		sb.append("price").append(kvsep).append(price).append(fieldSep);
		sb.append("commission").append(kvsep).append(commission).append(fieldSep);
		sb.append("value").append(kvsep).append(value).append(fieldSep);
		sb.append("remarks").append(kvsep).append(remarks).append(fieldSep);
		
		sb.append("date").append(kvsep).append(date).append(fieldSep);
		return sb.toString();
	}

}
