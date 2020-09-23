package com.codetutr.restAPI;

import java.util.ArrayList;

import com.codetutr.restAPI.response.TWMResponse;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class TransportAPI {
	
	public <T>TWMResponse<T> unmarshal(String input, Class<T> objectClass){
       ObjectMapper mapper = new ObjectMapper();
       TWMResponse<T> response = null;

       try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(TWMResponse.class,TWMResponse.class);
            response = mapper.readValue(input, javaType);
        } catch(Exception e){
            String errMsg = "Error with JSON unmarshal:" + e.getMessage() + " - Raw JSON response = " + input;
            throw new RuntimeException(errMsg);
         }
       return response;
    }
	
	public <T>TWMResponse<ArrayList<T>> unmarshalList(String input, Class<T> objectClass){
		ObjectMapper mapper = new ObjectMapper();
		TWMResponse<ArrayList<T>> mcResponse = null;
		
		try {
			// unmarshall TWMResponse only
			JavaType javaType = mapper.getTypeFactory().constructParametricType(TWMResponse.class, TWMResponse.class);
			TWMResponse<Object> response = mapper.readValue(input, javaType);
			
			// get the data attribute as JSON
			String dataJson = mapper.writeValueAsString(response.getData());
			
			// unmarshall the data as a list
			JavaType listType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ArrayList.class);
			ArrayList<T> listResponse = mapper.readValue(dataJson, listType);
			
			// map the full response
			mcResponse = new TWMResponse<ArrayList<T>>();
			mcResponse.setTransactionId(response.getTransactionId());
			mcResponse.setHostName(response.getHostName());
			mcResponse.setErrors(response.getErrors());
			mcResponse.setData(listResponse);
		} catch(Exception e) {
			String errMsg = "Error with JSON unmarshal:" + e.getMessage() + " -Raw JSON response = " + input;
			throw new RuntimeException(errMsg);
		}
		   return mcResponse;
	   }
}
