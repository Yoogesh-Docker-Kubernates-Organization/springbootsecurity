package com.codetutr.config.springMvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.codetutr.filter.LogFilter;
import com.codetutr.filter.SwaggerResponseFilter;

public class FilterConfig {

	@Bean
	public FilterRegistrationBean<LogFilter> log4jFilterBean() {
		FilterRegistrationBean<LogFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		LogFilter logFilter = new LogFilter();
		filterRegistrationBean.setFilter(logFilter);

		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		filterRegistrationBean.setUrlPatterns(urlPatterns);

		filterRegistrationBean.setOrder(1);
		return filterRegistrationBean;
	}
	
	@Bean
	public FilterRegistrationBean<SwaggerResponseFilter> swaggerResponseFilterBean() {
		FilterRegistrationBean<SwaggerResponseFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		SwaggerResponseFilter swaggerResponseFilter = new SwaggerResponseFilter();
		filterRegistrationBean.setFilter(swaggerResponseFilter);
		
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/forwardRequestViaFilter");
		filterRegistrationBean.setUrlPatterns(urlPatterns);
		
		filterRegistrationBean.setOrder(2);
		return filterRegistrationBean;
	}
}
