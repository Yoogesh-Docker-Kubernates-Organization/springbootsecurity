package com.codetutr.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.codetutr.stateMahine.PaymentState;

@Entity
public class Payment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)  // will save number in a database if you don't do this
	private PaymentState state;
	
	private BigDecimal amount;

	private String transactionToken;
	
	
	public Payment(){
	}
	
	public Payment(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PaymentState getState() {
		return state;
	}

	public void setState(PaymentState state) {
		this.state = state;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getTransactionToken() {
		return transactionToken;
	}

	public void setTransactionToken(String transactionToken) {
		this.transactionToken = transactionToken;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", state=" + state + ", amount=" + amount + ", transactionToken="
				+ transactionToken + "]";
	}
	
	
}
