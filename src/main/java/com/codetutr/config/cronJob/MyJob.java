package com.codetutr.config.cronJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

import com.nimbusds.oauth2.sdk.util.StringUtils;

public class MyJob {
	
	private static final String defaultMessage = "ConfigMap is currently disabled in this application. Don't forget to include \'spring-cloud-starter-kubernetes-config\' in a Pom.xml file to enable ConfigMap settings if you are running on Kubernetes Cluster.";
	private static final String configMap_Message = "kubernetes.message.on.the.fly";
	
	@Autowired
	private Environment env;
	
	/**
	 * To pass expression you can use @Scheduled(cron = "0 15 10 15 * ?")
	 * For more information See {@link https://www.baeldung.com/spring-scheduled-tasks}
	 */
	@Scheduled(fixedRate=100000)
	public void sendMessage() {
		String message = StringUtils.isBlank(env.getProperty(configMap_Message)) ? defaultMessage : env.getProperty(configMap_Message);
		System.err.println("Sending Messages in every 100 second............." + message);
	}
}
