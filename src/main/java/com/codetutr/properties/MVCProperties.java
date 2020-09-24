package com.codetutr.properties;

import com.codetutr.utility.AbstractProperties;

public class MVCProperties extends AbstractProperties {

	public static final String PROPERTY_FILE_NAME = "properties/mvc/mvc.properties";
	
	private static MVCProperties instance;
	
	private final int readTimeout;
	private final int connectionTimeout;
	private final String serviceBaseUrl;
	private final String serviceUsername;
	private final String servicePassword;
	
	protected MVCProperties() 
	{
		super(PROPERTY_FILE_NAME);
		
		readTimeout = getInt("core.service.read.timeout", 500);
		connectionTimeout = getInt("core.service.connection.timeout", 5000);
		serviceBaseUrl = getRequiredString("core.service.baseurl");
		serviceUsername = getRequiredString("core.service.username");
		servicePassword = getRequiredString("core.service.password");
	}

	public static synchronized MVCProperties getInstance(){
		if(instance == null){
			instance = new MVCProperties();
		}
		return instance;
	}
	
	public int getReadTimeout() {
		return readTimeout;
	}
	
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public String getBaseUrl() {
		return serviceBaseUrl;
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public String getServicePassword(){
		return servicePassword;
	}

	public static void setInstance(MVCProperties instance){
		MVCProperties.instance = instance;
	}
	
	/**
	 * This is used if callers want to dynamically do the lookup.
	 * Typically used if they combine a value to teh key and do the lookup
	 */
	public String getRequiredProperty(String key){
		return getRequiredString(key);
	}
	
	
	/**
	 * This is used if callers want to dynamically do the lookup.
	 * Typically used if they combine a value to teh key and do the lookup
	 */
	public String getProperty(String key, String defaultValue){
		return getString(key, defaultValue);
	}
}
