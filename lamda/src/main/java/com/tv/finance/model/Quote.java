package com.tv.finance.model;

import java.util.Date;

public class Quote {

	
	//SYMBOL	SERIES	OPEN	HIGH	LOW	CLOSE	LAST	PREVCLOSE	TOTTRDQTY	TOTTRDVAL	TIMESTAMP	TOTALTRADES	ISIN
	//20MICRONS	EQ	50.25	51.1	49.2	49.45	49.4	49.95	60512	3006601.05	06-Apr-18	491	INE144J01027
	
	private String code;
	
	private double open;
	/**
	 * The closing price of the day is the weighted average price of the last 30mins of trading.
	 */
	private double close;
	
	private double high;
	private double low;
	
	/**
	 * Last traded price of the day is the actual last traded price.
	 */
	private double last;
	
	private double prevclose;
	
	private int tradeQty;
	
	private double tradeValue;
	
	private int totTrades;
	
	private String isin;
	
	private Date date;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getLast() {
		return last;
	}

	public void setLast(double last) {
		this.last = last;
	}

	public double getPrevclose() {
		return prevclose;
	}

	public void setPrevclose(double prevclose) {
		this.prevclose = prevclose;
	}

	public int getTradeQty() {
		return tradeQty;
	}

	public void setTradeQty(int tradeQty) {
		this.tradeQty = tradeQty;
	}

	public double getTradeValue() {
		return tradeValue;
	}

	public void setTradeValue(double tradeValue) {
		this.tradeValue = tradeValue;
	}

	public int getTotTrades() {
		return totTrades;
	}

	public void setTotTrades(int totTrades) {
		this.totTrades = totTrades;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
