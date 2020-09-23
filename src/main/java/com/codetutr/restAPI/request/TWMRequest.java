package com.codetutr.restAPI.request;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class TWMRequest <T>  {
	
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	@Override
	public String toString() 
	{
		return ReflectionToStringBuilder.toString(this);
	}

}
