package com.codetutr.config.springMvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan(basePackages = "com.codetutr")
@Import({ContextConfig.class, FilterConfig.class, InterceptorConfig.class, InternationalizationConfig.class, StaticResourceConfig.class, ThemeConfig.class})
public class AppConfig_Mvc implements WebMvcConfigurer
{
	@Bean
	public ViewResolver viewResolver() 
	{
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) 
    {
    	registry.addViewController("/dispatcher").setViewName("/security/sign-in");
    }
}
