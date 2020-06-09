package com.codetutr.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codetutr.entity.User;
import com.codetutr.services.UserService;
import com.codetutr.utility.UtilityHelper;
import com.codetutr.validation.UserBeanValidator;

@Controller
public class SignupController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@InitBinder
	public void initBinder (WebDataBinder binder)
	{
			binder.setDisallowedFields(new String[] {"uid"});
	} 
	
	
	@Autowired
	@Qualifier("userBeanValidator")
	private UserBeanValidator userBeanValidator;

	
	@RequestMapping(value = "/sign-up", method = RequestMethod.GET)
	public ModelAndView Signup(HttpServletRequest request, @ModelAttribute("user") User user) 
	{
		ModelAndView mv = new ModelAndView ("security/sign-up");
		return mv;
	}
	
	
	@RequestMapping(value ="/sign-up", method = RequestMethod.POST)
	public String submitform(HttpServletRequest request, @ModelAttribute ("user") User user,BindingResult bindingResult, RedirectAttributes redirectAttributes, ModelMap model)
	{

		String redirectpath=null;
		
		userBeanValidator.validate(user, bindingResult);
		
		if(bindingResult.hasErrors())
		{
			// Forward errors back to the view
			redirectpath = "security/sign-up";
		}
		
		else if(userService.ismoreUsernameExists(user.getUsername()))
		{
			bindingResult.reject("validation.username.already.exists");
		    redirectpath =  "security/sign-up";
		}
		else
		{
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setEnabled(true);
			user.setAuthorities(UtilityHelper.getUserAuthList(user));
			
		    User persistedUser = userService.createUser(user);
		    request.setAttribute("user1",  persistedUser);
		    model.addAttribute("user",new User());   // to reset table
		    redirectpath =  "security/sign-up";
		}
		
		return redirectpath;
	}
	

}
