package com.codetutr.config.cronJob;

import org.springframework.scheduling.annotation.Scheduled;

public class MyJob {
	
	/**
	 * To pass expression you can use @Scheduled(cron = "0 15 10 15 * ?")
	 * For more information See {@link https://www.baeldung.com/spring-scheduled-tasks}
	 */
	@Scheduled(fixedRate=100000)
	public void sendMessage() {
		System.out.println("Sending Messages in every 100 second.............");
	}
}
