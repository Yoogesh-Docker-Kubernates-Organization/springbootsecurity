package com.codetutr.config.springMvc;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) 
    {
        registry
        	.addResourceHandler("/static/**")
        		.addResourceLocations("classpath:/static/");
        			//.setCachePeriod(3_155_6926);;
        
        /**
         * This is needed for webSocket to read the javascript files from  the jars
         */
        registry
        	.addResourceHandler("/webjars/**")
        		.addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
	
}
