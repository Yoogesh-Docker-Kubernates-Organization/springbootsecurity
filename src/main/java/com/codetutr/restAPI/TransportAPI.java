package com.codetutr.restAPI;

import java.util.ArrayList;
import java.util.Collection;

import com.codetutr.restAPI.response.TWMResponse;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.TypeFactory;

public abstract class TransportAPI {

	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * This method is used to marshal all the object that we know the types
	 */
	public <T> TWMResponse<T> unmarshal(String input, Class<T> objectClass) {
		TWMResponse<T> response = null;

		try {
			JavaType javaType = mapper.getTypeFactory().constructParametricType(TWMResponse.class, TWMResponse.class);
			response = mapper.readValue(input, javaType);
		} catch (Exception e) {
			String errMsg = "Error with JSON unmarshal:" + e.getMessage() + " - Raw JSON response = " + input;
			throw new RuntimeException(errMsg);
		}
		return response;
	}

	/**
	 * 
	 * This method is used to marshal all the object that we know the types
	 */
	public <T> TWMResponse<ArrayList<T>> unmarshalList(String input, Class<T> objectClass) {
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
		} catch (Exception e) {
			String errMsg = "Error with JSON unmarshal:" + e.getMessage() + " -Raw JSON response = " + input;
			throw new RuntimeException(errMsg);
		}
		return mcResponse;
	}

	/**
	 * 
	 * This method is used to marshal all the object that we don't know the types
	 */
	public static String encode(Object object) throws Exception {
		return mapper.writer().writeValueAsString(object);
	}

	/**
	 * 
	 * This method is used to unmarshal all the object that we don't know the types
	 */
	public static <T> T decode(String input, Class<T> objectClass) throws Exception {
		T output = null;

		if (input != null) {
			if (objectClass == null) {
				throw new IllegalArgumentException("The object class is null.");
			} else {
				ObjectReader objectReader = mapper.reader(objectClass);
				output = objectReader.readValue(input);
			}
		}
		return output;
	}

	/**
	 * 
	 * This method is used to unmarshal all the collections that we don't know the
	 * types
	 */
	public static <T> Collection<T> decodeCollection(String input, Class<T> type) throws Exception {
		Collection<T> collection = null;
		if (input != null) {
			TypeFactory typeFactory = mapper.getTypeFactory();
			JavaType javaType = typeFactory.constructParametrizedType(Collection.class, Collection.class, type);
			collection = mapper.readValue(input, javaType);
		}
		return collection;
	}

}
