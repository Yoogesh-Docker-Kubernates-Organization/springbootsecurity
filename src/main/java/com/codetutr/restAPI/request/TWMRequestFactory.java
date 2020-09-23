package com.codetutr.restAPI.request;

public class TWMRequestFactory {
	
	public static <T> TWMRequest<T>  getRequest(T data){
		TWMRequest <T> request = new TWMRequest<>();
		request.setData(data);
		return request;
	}
}