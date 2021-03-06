package com.codetutr.restAPI.response;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.codetutr.exception.model.ErrorResponse;
import com.codetutr.utility.ShortString;

public class TWMResponse<T> implements ShortString {

	private String transactionId;
	private String hostName;
	private boolean droolsRuleApplied;
	
	private T data;
	private ArrayList<ErrorResponse> errors;

	public TWMResponse() {
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ArrayList<ErrorResponse> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<ErrorResponse> errors) {
		this.errors = errors;
	}

	public boolean isDroolsRuleApplied() {
		return droolsRuleApplied;
	}

	public void setDroolsRuleApplied(boolean droolsRuleApplied) {
		this.droolsRuleApplied = droolsRuleApplied;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Override
	public String toShortString() {
		String shortString = null;
		if (data == null) {
			shortString = toString();
		} else {
			ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this);
			builder = builder.setExcludeFieldNames("data");

			if (data instanceof ShortString) {
				// call the toShortString() method for the data object
				shortString = StringUtils.chop(builder.toString()) + ",data=" + ((ShortString) data).toShortString() + "]";
			} else {
				// only use the data object className and hashCode
				shortString = StringUtils.chop(builder.toString()) + ",data=" + data.getClass().getName() + "@" + data.hashCode() + "]";
			}
		}
		return shortString;
	}
}
