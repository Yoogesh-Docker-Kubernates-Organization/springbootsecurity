package com.codetutr.restAPI;

import java.util.List;

import com.codetutr.entity.User;
import com.codetutr.properties.MVCProperties;
import com.codetutr.restAPI.response.TWMResponse;

public class CoreServiceClient extends TransportAPI {
	
	private final String serviceBaseUrl;
	private final int serviceReadTimeout;
	private final int connectionTimeout;
	private final BasicAuthenticationCredentials serviceCredentials;
	
	public CoreServiceClient(){
		
		MVCProperties mvcProperties = MVCProperties.getInstance();
		serviceBaseUrl = mvcProperties.getBaseUrl();
		serviceReadTimeout = mvcProperties.getReadTimeout();
		connectionTimeout = mvcProperties.getReadTimeout();
		serviceCredentials = new BasicAuthenticationCredentials(mvcProperties.getServiceUsername(), mvcProperties.getServicePassword());
	}
	
	public TWMResponse<List<User>> getAllUsers(){
		
		HttpAdapter httpAdapter = new HttpAdapter();
		String response = httpAdapter.executeGet(serviceBaseUrl + "/api/user", null, serviceReadTimeout, connectionTimeout, serviceCredentials);
		TWMResponse<List<User>> twmResponse = unmarshalList(response, User.class);
		return twmResponse;
	}

}
