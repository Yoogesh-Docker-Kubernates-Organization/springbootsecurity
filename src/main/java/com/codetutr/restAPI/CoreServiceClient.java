package com.codetutr.restAPI;

import java.util.ArrayList;

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
	
	public ArrayList<User> getAllUsers()
	{
		HttpAdapter httpAdapter = new HttpAdapter();
		String response = httpAdapter.executeGet(serviceBaseUrl + "/api/user/.json", null, serviceReadTimeout, connectionTimeout, serviceCredentials);
		TWMResponse<ArrayList<User>> twmResponse = unmarshalList(response, User.class);
		ArrayList<User> users = twmResponse.getData();
		return users;
	  }

}
