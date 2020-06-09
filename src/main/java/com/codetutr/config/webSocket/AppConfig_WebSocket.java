package com.codetutr.config.webSocket;

import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.codetutr.validationHelper.LemonConstant;

@EnableWebSocketMessageBroker
@Import({SimpMessageTemplateConfig.class})
public class AppConfig_WebSocket implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
			.addEndpoint(LemonConstant.WEB_SOCKET_ENDPOINT)
				.setAllowedOrigins("*")
					.withSockJS()
						.setClientLibraryUrl("/webjars/sockjs-client/1.1.2/sockjs.min.js");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		/**
		 * Enabling spring boot provided MessageQueue
		 * Make sure SpringBoot by-default creates a Queue named 'queue' and Topic named 'topic'
		 */
		registry.enableSimpleBroker(LemonConstant.WEB_SOCKET_QUEUE_NAME, LemonConstant.WEB_SOCKET_TOPIC_NAME);
		registry.setApplicationDestinationPrefixes(LemonConstant.WEB_SOCKET_DESTINATION_PREFIX);
	}

	
}
