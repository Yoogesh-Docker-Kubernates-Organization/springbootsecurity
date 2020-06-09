package com.codetutr.config.springMvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private ThemeChangeInterceptor themeChangeInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(themeChangeInterceptor);
		//You can add others here...
	}
}
