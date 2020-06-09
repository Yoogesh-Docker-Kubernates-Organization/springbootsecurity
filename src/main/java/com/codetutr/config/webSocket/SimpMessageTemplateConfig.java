package com.codetutr.config.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

@Configuration
public class SimpMessageTemplateConfig {
	
	/**
	 * 
	 * This template is automatically instantiated by Spring at runtime Using brokerMessagingTemplate
	 * So this bean is just need to be instantiated for Compile purpose
	 */
    @Bean(name="simpMessageTemplateForCompilePurpose")
    public SimpMessagingTemplate getSimpMessageBean() {
    	return new SimpMessagingTemplate(new ExecutorSubscribableChannel());
    }
}
