package com.codetutr.config.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class AppConfig_Swagger implements WebMvcConfigurer {
	
	/**
	 * 
	 * Swagger API 3.0 implementation
	 */
	@Bean
	public GroupedOpenApi goApiSchema() {
		String paths[] = {"/api/authentication/**", "/api/user/**"};
		return GroupedOpenApi.builder().group("GoLang").pathsToMatch(paths)
				.build();
	}
	
	@Bean
	public GroupedOpenApi webSocketSchema() {
		String paths[] = {"/api/websocket/**"};
		return GroupedOpenApi.builder().group("Websocket").pathsToMatch(paths)
				.build();
	}
	
	@Bean
	public GroupedOpenApi otherSchema() {
		String paths[] = {"/api/common/**"};
		return GroupedOpenApi.builder().group("Other").pathsToMatch(paths)
				.build();
	}

}
