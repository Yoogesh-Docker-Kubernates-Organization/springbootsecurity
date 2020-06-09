package com.codetutr.feature.webSockets;

import java.util.Date;

public class SimpleMessage {
	
	private Long uid;
	private String senderUsername;
	private String recieverUsername;
	private String message;
	private Date receivedDate;
	private Boolean joined = true;
	private Boolean isreplied = true;
	
	public SimpleMessage() {

	}

	public SimpleMessage(Long uid, String senderUsername, String recieverUsername, String message, Date receivedDate, Boolean isreplied) {
		this.uid = uid;
		this.senderUsername = senderUsername;
		this.recieverUsername = recieverUsername;
		this.message = message;
		this.receivedDate = receivedDate;
		this.isreplied = isreplied;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	public String getRecieverUsername() {
		return recieverUsername;
	}

	public void setRecieverUsername(String recieverUsername) {
		this.recieverUsername = recieverUsername;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Boolean getIsreplied() {
		return isreplied;
	}

	public void setIsreplied(Boolean isreplied) {
		this.isreplied = isreplied;
	}
	
	public Boolean getJoined() {
		return joined;
	}

	public void setJoined(Boolean joined) {
		this.joined = joined;
	}

	@Override
	public String toString() {
		return "SimpleMessage [uid=" + uid + ", senderUsername=" + senderUsername + ", recieverUsername="
				+ recieverUsername + ", message=" + message + ", receivedDate=" + receivedDate + ", isreplied="
				+ isreplied + "]";
	}

}
