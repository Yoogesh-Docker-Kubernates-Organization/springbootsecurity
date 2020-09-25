package com.codetutr.exception.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Error DTO, to be sent as response body
 * in case of errors
 */
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {
	
	private String transactionId;
	private String exceptionId;
	private String error;
	private String message;
	private Integer status; // We'd need it as integer in JSON serialization
	private Collection<LemonFieldError> errors;
	
	public boolean incomplete() {
		return message == null || status == null;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Collection<LemonFieldError> getErrors() {
		return errors;
	}

	public void setErrors(Collection<LemonFieldError> errors) {
		this.errors = errors;
	}
}
