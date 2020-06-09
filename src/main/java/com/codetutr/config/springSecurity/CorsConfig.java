package com.codetutr.config.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codetutr.model.Cors;

public class CorsConfig implements WebMvcConfigurer {
	
	@Autowired
	Cors cors = new Cors();
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
        registry.addMapping("/**")
	        .allowedOrigins(cors.getAllowedOrigins())
	        .allowedMethods(cors.getAllowedMethods())
	        .allowedHeaders(cors.getAllowedHeaders())
	        .exposedHeaders(cors.getExposedHeaders())
	        .allowCredentials(cors.isAllowCredentials())
	        .maxAge(cors.getMaxAge());
	}
	
}
