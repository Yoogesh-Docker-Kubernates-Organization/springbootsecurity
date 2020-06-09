package com.codetutr.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractProperties 
{
	private final Logger logger = LoggerFactory.getLogger(AbstractProperties.class);
	
	protected final String propertyFileName;
	protected final Properties properties;
	
	
	protected AbstractProperties(String propertyFileName)
	{
		this.propertyFileName = propertyFileName;
		this.properties = loadProperties(propertyFileName);
	}
	
	
	private Properties loadProperties(String  propertyFileName)
	{
		Properties properties = new Properties();
		loadProperties(propertyFileName, properties);
		return properties;
	}

	
	
	protected int getInt(String key, int defaultValue)
	{
		int value = defaultValue;
		try
		{
			value = getRequiredInt(key);
		}
		
		catch(Exception e)
		{
			logger.error("Could not find a value for " + key + ". using default value: " + defaultValue);
		}
		
		return value;
	}
	
	protected  int getRequiredInt(String key)
	{
		int value;
		
		String sValue = getRequiredString(key);
		
		try
		{
			value = Integer.parseInt(sValue);
		}
		
		catch(Exception e)
		{
			throw new RuntimeException("The value for property" + key + " was not numeric in" + propertyFileName + ".");
		}
		
		return value;
	}
	

protected boolean getBoolean(String key, boolean defaultValue)
{
	boolean value = false;
	
	String stringValue = getString(key, null);
	
	if(stringValue == null)
	{
		value = defaultValue;
	}
	
	else
	{
		value = Boolean.valueOf(stringValue);
	}
	
	return value;
}


protected boolean getRequiredBoolean(String key)
{
	String stringValue = getRequiredString(key);
	
	boolean value = Boolean.valueOf(stringValue);
	
	return value;
}


protected String getString(String key, String defaultValue)
{
	String value = defaultValue;
	try
	{
		value = getRequiredString(key);
	}
	
	catch(Exception e)
	{
		logger.error("Could not find a value for " + key + ". using default value: " + defaultValue);
	}
	
	return value;
}


protected String getRequiredString(String key)
{
	String value = properties.getProperty(key);
	
	if((value = StringUtils.trimToNull(value)) == null)
	{
		throw new RuntimeException("The value for property" + key + " was not found in " + propertyFileName + ".");
	}
	
	return value;
}


protected Set<String> getAllValuesForProperties(String keyPrefix)
{
	Set<String> valueSet = new HashSet<>();
	
	Set<String> keySet = properties.stringPropertyNames();
	
	for(String key : keySet)
	{
		if(StringUtils.startsWith(key, keyPrefix))
		{
			String combinedValues = properties.getProperty(key);
			
			if((combinedValues = StringUtils.trimToNull(combinedValues)) != null)
			{
				String[] values = combinedValues.split(";");
				
				for(String value : values)
				{
					if((value = StringUtils.trimToNull(value)) != null)
				      {
						valueSet.add(value);
					  }	
				}
			}
		}
	}
	return valueSet;
}



protected Map<String, String> getAllEntriesForProperties(String keyPrefix)
{
	Map<String, String> matchingEntries = new HashMap<>();
	
	Set<Map.Entry<Object, Object>> allEntries = properties.entrySet();
	
	for(Map.Entry<Object, Object> entry :  allEntries)
	{
		String key = (String)entry.getKey();
		
		if(StringUtils.startsWith(key, keyPrefix))
		   {
			 String value = (String)entry.getValue();
			 
			 matchingEntries.put(key, value);
	       }
		
	}
	return matchingEntries;
}


private void loadProperties(String propertyFileName, Properties properties) 
{
	ClassLoader classLoader = AbstractProperties.class.getClassLoader();
	URL propertyFileURL = classLoader.getResource(propertyFileName);
	
	
	while(propertyFileURL == null)
	{
		classLoader = classLoader.getParent();
		if(classLoader == null)
		{
			break;
		}
		
		else
		{
			propertyFileURL = classLoader.getResource(propertyFileName);
		}
	}
	
	if(propertyFileURL ==null)
	{
		throw new RuntimeException("The property file could not be loaded by the class loader. please verity that " + propertyFileName + " exists on the class path.");
	}
	
	else
	{
		String propertyFilePath = propertyFileURL.getFile();
		if((propertyFilePath = StringUtils.trimToNull(propertyFilePath)) == null)
		{
			throw new RuntimeException("The property file path is null. please verify that " + propertyFileName + " is a valid file.");
		}
		
		else
		{
			try
			{
				propertyFilePath = URLDecoder.decode(propertyFilePath, "UTF-8");
			}
			
			catch(UnsupportedEncodingException exception)
			{
				throw new RuntimeException(exception);
			}
			
			try(FileInputStream propertyFileInputStream = new FileInputStream(propertyFilePath))
			{
				properties.load(propertyFileInputStream);
			}
			catch(IOException exception)
			{
				handleWhileReadingRunningFromCommandLine(properties, propertyFilePath);
			}
		}
	}
}


private void handleWhileReadingRunningFromCommandLine(Properties properties, String propertyFilePath) 
{
	propertyFilePath = getPropertyFilepathForJar(propertyFilePath);
	try(FileInputStream propertyFileInputStream = new FileInputStream(propertyFilePath)) 
	{
		properties.load(propertyFileInputStream);
	}
	catch(IOException ex) 
	{
		throw new RuntimeException("Exception occured while reading file from updated path : " + propertyFilePath);
	}
}


/**
 * While Running an application from command line, above way doesn't work. so it needs below logic.
 */
protected String getPropertyFilepathForJar(String propertyFilePath) {
	logger.debug("Original path before updating a path : " + propertyFilePath);
	String stringBeforeTarget = StringUtils.substringBefore(propertyFilePath, "target");
	String StringAfterClasses = StringUtils.substringAfter(propertyFilePath, "classes");
	String StringAfterRemovingFileColon = RegExUtils.replaceAll(stringBeforeTarget, "file:", "");
	String StringAfterRemovingExplanation = RegExUtils.replaceAll(StringAfterClasses, "!", "");
	String result = RegExUtils.replaceAll(StringAfterRemovingFileColon + "target\\classes" + StringAfterRemovingExplanation, "\\\\", "/");
	logger.debug("Path after modifying : " + result);
	return result;
}


/**
 * this will load the properties file which is inside the included Jar
 * Instead of getting a path and converting it to inputStream, we are directly getting inputStream here and loading it
 * We are not using this here though!! however just FYI this will also work
 * 
 * @param propertyFileName
 * @param properties
 */
@SuppressWarnings("unused")
private void loadPropertiesForGlobalPropertiesFileForLogging(String propertyFileName, Properties properties)
{
	try 
	{
		properties.load(URLClassLoader.getSystemResourceAsStream(propertyFileName));
	} 
	catch (IOException exception) 
	{
		throw new RuntimeException(exception);
	}
}

}
