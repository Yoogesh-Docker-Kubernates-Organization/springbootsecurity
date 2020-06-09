package com.codetutr.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("custom.cors")
public class Cors {

	/**
	 * Comma separated whitelisted URLs for CORS.
	 * Should contain the applicationURL at the minimum.
	 * Not providing this property would disable CORS configuration.
	 */
	private String[] allowedOrigins;
	
	
	/**
	 * Methods to be allowed, e.g. GET,POST,...
	 */	
	private String[] allowedMethods;
	
	
	/**
	 * Request headers to be allowed, e.g. content-type,accept,origin,x-requested-with,...
	 */	
	private String[] allowedHeaders;
	
	/**
	 * Response headers that you want to expose to the client JavaScript programmer, e.g. Lemon-Authorization.
	 * I don't think we need to mention here the headers that we don't want to access through JavaScript.
	 * Still, by default, we have provided most of the common headers.
	 *  
	 * <br>
	 * See <a href="http://stackoverflow.com/questions/25673089/why-is-access-control-expose-headers-needed#answer-25673446">
	 * here</a> to know why this could be needed.
	 */		
	private String[] exposedHeaders;
	
	
	/**
	 * CORS <code>maxAge</code> long property
	 */	
	private long maxAge;
	
	private boolean allowCredentials;
	
	
	public String[] getAllowedOrigins() {
		return allowedOrigins;
	}
	public void setAllowedOrigins(String[] allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}
	public String[] getAllowedMethods() {
		return allowedMethods;
	}
	public void setAllowedMethods(String[] allowedMethods) {
		this.allowedMethods = allowedMethods;
	}
	public String[] getAllowedHeaders() {
		return allowedHeaders;
	}
	public void setAllowedHeaders(String[] allowedHeaders) {
		this.allowedHeaders = allowedHeaders;
	}
	public String[] getExposedHeaders() {
		return exposedHeaders;
	}
	public void setExposedHeaders(String[] exposedHeaders) {
		this.exposedHeaders = exposedHeaders;
	}
	public long getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(long maxAge) {
		this.maxAge = maxAge;
	}
	public boolean isAllowCredentials() {
		return allowCredentials;
	}
	public void setAllowCredentials(boolean allowCredentials) {
		this.allowCredentials = allowCredentials;
	}
}
