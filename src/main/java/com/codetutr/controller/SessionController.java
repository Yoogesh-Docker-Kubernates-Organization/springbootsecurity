package com.codetutr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/sessions")
@ApiIgnore
public class SessionController {

	private final SessionRegistry sessionRegistry;

	@Autowired
	public SessionController(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<SessionInformation> sessions(Authentication authentication, ModelMap model) {
		List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
		model.put("sessions", sessions);
		return sessions;
	}

	@RequestMapping(value = "/sessions/{sessionId}", method = RequestMethod.DELETE)
	public String removeSession(@PathVariable String sessionId, RedirectAttributes redirectAttrs) {
		SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
		if (sessionInformation != null) {
			sessionInformation.expireNow();
		}
		redirectAttrs.addFlashAttribute("message", "Session was removed");
		return "redirect:/user/sessions/";
	}
}
