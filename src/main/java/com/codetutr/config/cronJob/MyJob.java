package com.codetutr.config.cronJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

import com.nimbusds.oauth2.sdk.util.StringUtils;

public class MyJob {
	
	private static final String configMap_Message = "kubernetes.message.on.the.fly";
	@Autowired
	private Environment env;
	
	/**
	 * To pass expression you can use @Scheduled(cron = "0 15 10 15 * ?")
	 * For more information See {@link https://www.baeldung.com/spring-scheduled-tasks}
	 */
	@Scheduled(fixedRate=100000)
	public void sendMessage() {
		String message = StringUtils.isBlank(env.getProperty(configMap_Message)) ? "Your application is currently Running on a Local server.Don't forget to Enable the Spring-cloud-kubernetes properties for ConfigMap settings when running in a Kubernetes Cluster." : env.getProperty(configMap_Message);
		System.out.println("Sending Messages in every 100 second............." + message);
	}
}
