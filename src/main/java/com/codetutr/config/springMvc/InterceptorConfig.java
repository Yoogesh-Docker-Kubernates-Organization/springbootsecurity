package com.codetutr.config.springMvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

import com.codetutr.interceptor.RequestParamValidationInterceptor;

public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private ThemeChangeInterceptor themeChangeInterceptor;
	@Autowired
	private RequestParamValidationInterceptor requestParamValidationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(themeChangeInterceptor);
		registry.addInterceptor(requestParamValidationInterceptor);
		//You can add others here...
	}
	
}
