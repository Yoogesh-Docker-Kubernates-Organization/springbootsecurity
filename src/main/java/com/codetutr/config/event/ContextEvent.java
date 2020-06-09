package com.codetutr.config.event;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

import com.codetutr.properties.LogProperties;
import com.codetutr.properties.EnvTargetProperties;

public class ContextEvent implements ApplicationContextInitializer<ConfigurableApplicationContext> 
{
    private final Logger logger = LoggerFactory.getLogger(ContextEvent.class);
    
    private static final String ENV_TARGET = "envTarget";

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) 
    {
        final ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String currentEnvTarget = getProfiletoBeSet(environment);
		setActiveProfile(environment);
		setSystemPropertiesforLogging(currentEnvTarget);
    }
	
	private String getProfiletoBeSet(final ConfigurableEnvironment environment) 
    {
        String target = environment.getProperty(ENV_TARGET);
        if (target == null) {
            logger.warn("User didn't enter the {} value while running a server!, Reading from env.properties now", ENV_TARGET);
            target = readEnvTargetFromPropertiesFile(target);
        }
        try {
        	logger.info("Active profile is being read from /properties/profile/profile-{}.properties file.", target);
			environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:properties/profile/profile-" + target + ".properties"));
		} catch (IOException e) {
			logger.error("Error occured while reading /properties/profile/profile-{}.properties file to get active profile.", target);
			e.printStackTrace();
		}
        return target;
    }

	private String readEnvTargetFromPropertiesFile(String target) 
	{
		try {
			target = EnvTargetProperties.getInstance().getEnvTarget();
		}
		catch(RuntimeException ex) {
		   logger.info("Could not read /properties/env.properties file. setting a default target as local. Currently this is occuring while running jar from commandLine.", target);
		   target = "local";
		}
		System.setProperty(ENV_TARGET, target);
		return target;
	}
	
    private void setActiveProfile(ConfigurableEnvironment environment) {
    	final String activeProfiles = environment.getProperty("spring.profiles.active");
    	String[] profiles = activeProfiles.split(",");
    	environment.setActiveProfiles(profiles);
    	logger.info("Current active profiles are: {}", Arrays.asList(profiles));
	}
    
	private void setSystemPropertiesforLogging(String currentEnvTarget) 
	{
		logger.info("setting system properties for RuntimeEnvironment as {}", null != currentEnvTarget ? currentEnvTarget.toUpperCase() : currentEnvTarget);
		System.setProperty(LogProperties.CURRENT_ENVIRONMENT, currentEnvTarget);
	}
}
