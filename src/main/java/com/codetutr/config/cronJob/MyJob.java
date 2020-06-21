package com.codetutr.config.cronJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

public class MyJob {
	
	@Autowired
	private Environment env;
	
	/**
	 * To pass expression you can use @Scheduled(cron = "0 15 10 15 * ?")
	 * For more information See {@link https://www.baeldung.com/spring-scheduled-tasks}
	 */
	@Scheduled(fixedRate=100000)
	public void sendMessage() {
		System.out.println("Sending Messages in every 100 second............." + env.getProperty("refreshScope.message.on.the.fly") + "   Environment: " + env.getProperty("envTarget"));
	}
}
