package com.codetutr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.codetutr.entity.User;
import com.codetutr.restAPI.CoreServiceClient;
import com.codetutr.restAPI.response.TWMResponse;

@Controller
public class DefaultPageController 
{			
	@GetMapping(value="/")
	public String homePage(ModelMap model, HttpServletRequest request, HttpServletResponse response) 
	{
		return "security/sign-in";
	}
	
	@GetMapping(value="/restclient")
	public TWMResponse<List<User>> testForRestAPI() {
		return new CoreServiceClient().getAllUsers();
	}
}



