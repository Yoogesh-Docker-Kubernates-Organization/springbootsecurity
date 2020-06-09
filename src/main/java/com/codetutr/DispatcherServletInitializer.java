package com.codetutr;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

import com.codetutr.config.cronJob.AppConfig_CronJob;
import com.codetutr.config.database.AppConfig_Persistance;
import com.codetutr.config.event.ContextEvent;
import com.codetutr.config.exception.AppConfig_Exception;
import com.codetutr.config.springMvc.AppConfig_Mvc;
import com.codetutr.config.springSecurity.AppConfig_Security;
import com.codetutr.config.springStateMachine.AppConfig_StateMachine;
import com.codetutr.config.swagger.AppConfig_Swagger;
import com.codetutr.config.webSocket.AppConfig_WebSocket;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
@AutoConfigureBefore({WebMvcAutoConfiguration.class,ErrorMvcAutoConfiguration.class, SecurityAutoConfiguration.class})
public class DispatcherServletInitializer extends SpringBootServletInitializer
{
	private final static Class<?>[] CONFIGS = {
		DispatcherServletInitializer.class,
		MyDispatcher.class,
		
		/**
		 * Initializes the Persistence Configuration
		 */
		AppConfig_Persistance.class,
		
		/**
		 * Initializes Spring-Security
		 */
		AppConfig_Security.class,
		
        /**
         * Initializes Spring-Mvc
         */
		AppConfig_Mvc.class,
		
        /**
         * Initializes Swagger configuration
         */
		AppConfig_Swagger.class,
		
		/**
		 * Initializes WebSocket configuration
		 */
		AppConfig_WebSocket.class,
		
		
		/**
		 * Initializes the cronJob configuration (In spring term though it is called Task Scheduler)
		 */
		AppConfig_CronJob.class,
		
		/**
		 * Initializes the spring state machine
		 */
		AppConfig_StateMachine.class,
		
		/**
		 * Initializes the exception
		 */
		AppConfig_Exception.class
	};

	public static void main(String... args) {
		final SpringApplication springApplication = new SpringApplication(CONFIGS);
        springApplication.addInitializers(new ContextEvent());
        springApplication.run(args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CONFIGS).initializers(new ContextEvent());
	}
}

class MyDispatcher
{
	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}
	
	@Bean
	public ServletRegistrationBean<DispatcherServlet> dispatcherServletRegistration() {
		final ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<DispatcherServlet>(dispatcherServlet(), "/");
		
		final Map<String, String> params = new HashMap<String, String>();
		params.put("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
		params.put("contextConfigLocation", "org.spring.sec2.spring");
		params.put("dispatchOptionsRequest", "true");
		registration.setInitParameters(params);
		
		registration.setLoadOnStartup(1);
		return registration;
	}
}
