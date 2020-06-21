package com.codetutr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RefreshScope
public class DefaultPageController 
{		
	private final Logger logger = LoggerFactory.getLogger(DefaultPageController.class);
	
	@Value("${refreshScope.message.on.the.fly}")
	private String message;
	
	@GetMapping(value="/")
	public String homePage(ModelMap model, HttpServletRequest request, HttpServletResponse response) 
	{
		return "security/sign-in";
	}
	
	@GetMapping(value="/forwardRequestViaFilter")
	public String forwardRequest() {
		logger.info("refreshScope.message.on.the.fly = {}", message);
		return "forward:/v2/api-docs";
	}
}
