package com.codetutr;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * This is done to register the {@link springSecurityFilterChain} with the war.Not suprisingly, Spring Security provides a base class 
 * {@link AbstractSecurityWebApplicationInitializer} that will ensure the {@link springSecurityFilterChain} gets registered for you. <p>
 * 
 * This configuration is optional for Spring-Boot since it provides this configuration out-of-the-box.<p>
 */
@Order(1)
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
	
	public SpringSecurityInitializer(){
		super();
	}
}
