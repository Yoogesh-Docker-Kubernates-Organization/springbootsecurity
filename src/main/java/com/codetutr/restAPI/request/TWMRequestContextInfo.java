package com.codetutr.restAPI.request;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class TWMRequestContextInfo implements Serializable {
	
	private static final long serialVersionUID = -720689049778027848L;
	
	private String transactionId;
	private String ipAddress;
	private String browserAgent;
	private String triggeredById;

	public TWMRequestContextInfo(){
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getBrowserAgent() {
		return browserAgent;
	}

	public void setBrowserAgent(String browserAgent) {
		this.browserAgent = browserAgent;
	}

	public String getTriggeredById() {
		return triggeredById;
	}

	public void setTriggeredById(String triggeredById) {
		this.triggeredById = triggeredById;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);

	}

}
