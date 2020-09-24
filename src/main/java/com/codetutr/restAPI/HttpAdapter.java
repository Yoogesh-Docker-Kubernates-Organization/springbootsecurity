package com.codetutr.restAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.codetutr.restAPI.request.TWMRequest;
import com.codetutr.restAPI.request.TWMRequestContextInfo;
import com.codetutr.validationHelper.LemonConstant;

public class HttpAdapter {
	
	private final Logger logger = LoggerFactory.getLogger(HttpAdapter.class);
	
	public <T> String executePost(String url, TWMRequest<T> request, int readTimeout, int connectionTimeout, BasicAuthenticationCredentials credentials)
	{
		String response = null;
		TWMRequestContextInfo requestContextInfo = RequestHelper.getCurrentRequestBean();
		try { 
		   HttpHeaders headers = createHeaders(credentials, requestContextInfo);
		   HttpEntity<TWMRequest<T>> httpEntity = new HttpEntity<> (request, headers);
		   
		   RestTemplate restTemplate = getRestTemplate(readTimeout, connectionTimeout);
		   ResponseEntity<String> responseEntity  = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
		   
		   response = responseEntity.getBody();
		} catch(Exception e){
			logger.error("transactionId: " + requestContextInfo.getTransactionId() + " -- HttpAdapter error: " + e);
			throw new RuntimeException(e.getMessage());
		}
		return response;
	}
	
	public String executeGet(String url, ArrayList<Pair<String, String>> nameValuePairs, int readTimeout, int connectionTimeout, BasicAuthenticationCredentials credentials)
	{
		String response = null;
		
		String fullUrl = url;
		TWMRequestContextInfo requestContextInfo = RequestHelper.getCurrentRequestBean();
		for(Pair<String, String> nameValuePair : nameValuePairs){
			/**
			 * Have to uri encode the values ( not urlencode) in case they have special uri/url chars urlencode will change spaces to + instead of %20. + works goeed in querystring values,
			 * but since we are sending these up as part of the uri and not querystring need them to be %20 otherwise the jax-rs pathparam annotation will not decode them
			 */
			String value = nameValuePair.getRight();
			fullUrl = StringUtils.replace(fullUrl, nameValuePair.getLeft(), value);
		}
		
		try {   
			HttpHeaders headers = createHeaders(credentials, requestContextInfo);
			HttpEntity<String> httpEntity = new HttpEntity<>(headers);
			
			RestTemplate restTemplate = getRestTemplate(readTimeout, connectionTimeout);
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
			
			response = responseEntity.getBody(); 
		} catch(Exception e) {
			logger.error("transactionId: " + requestContextInfo.getTransactionId() + " -- HttpAdapter error: " + e);
			throw new RuntimeException(e.getMessage());
		}
		return response;
	}
	
	
		
	private RestTemplate getRestTemplate(int readTimeout, int connectionTimeout){
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setReadTimeout(readTimeout);
		requestFactory.setConnectTimeout(connectionTimeout);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}
	
	private HttpHeaders createHeaders(BasicAuthenticationCredentials credentials, TWMRequestContextInfo requestContextInfo){
		HttpHeaders httpHeaders = new HttpHeaders();
		if(credentials !=null){
			List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
			acceptableMediaTypes .add(MediaType.APPLICATION_XML);
			acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(acceptableMediaTypes);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			httpHeaders.add("Authorization", credentials.toAuthorizationHeaderValue());
		}
		if(requestContextInfo != null){
			try {
				httpHeaders.add(LemonConstant.TWM_REQUEST_CONTEXT_INFO_HEADER_NAME, TransportAPI.encode(requestContextInfo));
			} catch(Exception exception) {
				logger.error("An exception occurred marshalling the request context into header value.", exception);
				throw new RuntimeException(exception.getMessage());
			}
		}
		return httpHeaders;
	}

}
