package com.codetutr.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codetutr.entity.User;
import com.codetutr.model.annotation.AdminUser;
import com.codetutr.model.annotation.AuthenticatedUser;
import com.codetutr.model.annotation.DbaUser;
import com.codetutr.model.annotation.NormalUser;
import com.codetutr.model.annotation.PostFilterEvents;
import com.codetutr.model.annotation.PreFilterEvents;
import com.codetutr.model.annotation.UserWithEditPermission;
import com.codetutr.utility.UtilityHelper;
import com.codetutr.utility.Event;

@Service
public class SecuredMethodService {
	
	@DbaUser
	public String dbaOnly() {
		return "dba() method is called Successfully";
	}
	
	@AdminUser
	public String adminOnly() {
		return "admin() method is called Successfully";
	}
	
	@NormalUser
	public String userOnly() {
		return "user() method is called Successfully";
	}
	
	@AuthenticatedUser
	public String authenticatedOnly() {
		return "authenticated() method is called Successfully";
	}
	
	@UserWithEditPermission
	public String userWithEditPermission(User user) {
		return "Congratulation Dear " + user.getUsername() + "!! You have an Edit permission!";
	}

	@PostFilterEvents
	public List<Event> getEvents() {
		return UtilityHelper.addEventsIntoList(new LinkedList<>());
	}

	@PreFilterEvents
	public List<Event> saveEvents(List<Event> events) {
		return events;
	}
}
