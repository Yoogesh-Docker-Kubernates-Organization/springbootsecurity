package com.codetutr.restAPI.response;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.codetutr.exception.model.ErrorResponse;
import com.codetutr.utility.HostnameHelper;
import com.codetutr.utility.UtilityHelper;

public class TWMResponseFactory {
	
	public static <T>TWMResponse<T> getResponse(T data)
	{
		TWMResponse<T> response = new TWMResponse<>();
		response.setData(data);
		response.setTransactionId(UtilityHelper.generateFullUUIDWithOutDashes());
		response.setHostName(getHostname());
		return response;
	}
	
	public static <Type> TWMResponse<Type> getResponse(Type data, ArrayList<ErrorResponse> error)
	{
		TWMResponse<Type> response = new TWMResponse<>();
		response.setData(data);
		response.setTransactionId(UtilityHelper.generateFullUUIDWithOutDashes());
		response.setHostName(getHostname());
		response.setErrors(error);
		return response;
	}
	
	
	private static String getHostname()
	{
		HostnameHelper hostnameHelper = HostnameHelper.getInstance();
		String hostname = hostnameHelper.getLocalHostname();
		
		if((hostname = StringUtils.trimToNull(hostname)) == null){
			hostname = "N/A";
		}
		return hostname;
	}
}
