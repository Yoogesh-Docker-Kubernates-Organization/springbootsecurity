package com.codetutr.restAPI.response;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.codetutr.exception.model.ErrorResponse;
import com.codetutr.restAPI.RequestHelper;
import com.codetutr.utility.HostnameHelper;

public class TWMResponseFactory {
		
	public static <T>TWMResponse<T> getResponse(T data, HttpServletRequest request){
		TWMResponse<T> response = new TWMResponse<>();
		response.setData(data);
		response.setTransactionId(RequestHelper.getTransactionId());
		response.setHostName(getHostname());
		return response;
	}
	
	public static <Type> TWMResponse<Type> getResponse(Type data, ArrayList<ErrorResponse> error){
		TWMResponse<Type> response = new TWMResponse<>();
		response.setData(data);
		response.setTransactionId(RequestHelper.getTransactionId());
		response.setHostName(getHostname());
		response.setErrors(error);
		return response;
	}
	
	private static String getHostname(){
		HostnameHelper hostnameHelper = HostnameHelper.getInstance();
		String hostname = hostnameHelper.getLocalHostname();
		if(StringUtils.isBlank(hostname))
			hostname = "SpringBootSecurity";
		return hostname;
	}
}
