package com.codetutr.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.entity.Authority;
import com.codetutr.entity.User;
import com.codetutr.services.SecuredMethodService;
import com.codetutr.utility.UtilityHelper;
import com.codetutr.utility.Event;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/secure")
@ApiIgnore
public class SecuredMethodController {

	@Autowired
	SecuredMethodService securedService;
	

	@RequestMapping(value = "/dba", method = RequestMethod.GET)
	public String secureDba() {
		return securedService.dbaOnly();
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String secureAdmin() {
		return securedService.adminOnly();
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String secureUser() {
		return securedService.userOnly();
	}
	
	@RequestMapping(value = "/authenticated", method = RequestMethod.GET)
	public String authenticatedUser() {
		return securedService.authenticatedOnly();
	}
	
	
	/**
	 * You may notice that Spring MVC can automatically obtain the Authentication object for us. This is because Spring Security maps our current 
	 * Authentication object to the HttpServletRequest.getPrincipal() method
	 */
	@RequestMapping(value = "/editPermission", method = RequestMethod.GET)
	public String userWithEditPermission(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		User user = new User(null, request.getRemoteUser(), null, null, null, true, new ArrayList<Authority>());
		for(GrantedAuthority next: authentication.getAuthorities()) {
			if(next.getAuthority().equals("ROLE_DBA")) {
				user.setAuthorities(UtilityHelper.getDbaAuthList(user));
				break;
			}
		}
		return securedService.userWithEditPermission(user);
	}
	
	@RequestMapping(value = "/events-preFilter", method = RequestMethod.GET)
	public List<Event> saveEvents() {
		List<Event> events = UtilityHelper.addEventsIntoList(new LinkedList<>());
		return securedService.saveEvents(events);
	}

	@RequestMapping(value = "/events-postFilter", method = RequestMethod.GET)
	public List<Event> getEvents() {
		 return securedService.getEvents();
	}
	
}
