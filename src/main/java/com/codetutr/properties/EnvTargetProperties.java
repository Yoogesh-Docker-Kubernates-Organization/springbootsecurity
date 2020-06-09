package com.codetutr.properties;

import com.codetutr.utility.AbstractProperties;

public class EnvTargetProperties extends AbstractProperties{

	public static final String PROPERTY_FILE_NAME = "properties/env.properties";
	private static EnvTargetProperties instance;
	
	private final String envTarget;
	
	protected EnvTargetProperties() 
	{
		super(PROPERTY_FILE_NAME);
		
		envTarget = getString("custom.envTarget.value", "local");
	}

	public static synchronized EnvTargetProperties getInstance()
	{
		if(instance == null)
		{
			instance = new EnvTargetProperties();
		}
		return instance;
	}
	
	public String getEnvTarget() {
		return envTarget;
	}

}
