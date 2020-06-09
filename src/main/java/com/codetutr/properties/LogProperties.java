package com.codetutr.properties;

import org.apache.commons.lang3.StringUtils;

import com.codetutr.utility.AbstractProperties;

public class LogProperties extends AbstractProperties
{
	public static final String PROPERTY_FILE_NAME = "properties/log/log.properties";
	public final static String CURRENT_ENVIRONMENT = "RuntimeEnvironment";
	private static LogProperties instance;
	
	
	//for Loggings
	private final String runtimeEnvironment;
	private final String defaultConfigDirectoryForLogging;
	private final String defaultConfigFileNameForLogging;
	private final String customConfigFileNameForBase;

	
	
	private LogProperties()
	{
		super(PROPERTY_FILE_NAME);
		
		runtimeEnvironment = getString("logging.environment.runtimeEnvironment", "local");
		defaultConfigDirectoryForLogging = getString("logging.default.config.directory.for.logging", "/admin");
		defaultConfigFileNameForLogging = getString("logging.default.config.filename.for.logging", "log4j.config");
		customConfigFileNameForBase = getString("logging.custom.config.filename.for.base", "log4j.");
	}
	
	
	public static synchronized LogProperties getInstance()
	{
		if(instance == null)
		{
			instance = new LogProperties();
		}
		return instance;
	}
	
	
	
	
	
	/**
	 * Getters only
	 * 
	 */
	public String getRuntimeEnvironment() 
	{
		String result = null;
		
		String currentEnvironment = System.getProperty(CURRENT_ENVIRONMENT);
		if(!StringUtils.isEmpty(currentEnvironment))
		{
			result = currentEnvironment;
		}
		else
		{
			result = this.runtimeEnvironment;
		}
		return result;
	}
	

	public String getDefaultConfigDirectoryForLogging() 
	{
		return defaultConfigDirectoryForLogging;
	}

	public String getDefaultConfigFileNameForLogging() 
	{
		return defaultConfigFileNameForLogging;
	}

	public String getCustomConfigFileNameForBase() 
	{
		return customConfigFileNameForBase;
	}

}
