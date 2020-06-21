package com.codetutr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultPageController 
{			
	@GetMapping(value="/")
	public String homePage(ModelMap model, HttpServletRequest request, HttpServletResponse response) 
	{
		return "security/sign-in";
	}
	
	@GetMapping(value="/forwardRequestViaFilter")
	public String forwardRequest() {
		return "forward:/v2/api-docs";
	}
}



