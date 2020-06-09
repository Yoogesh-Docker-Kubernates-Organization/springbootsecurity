package com.codetutr.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OAuth2Controller {
	
    private ClientRegistrationRepository clientRegistrationRepository;
	private Environment env;
	
	@Autowired
	public OAuth2Controller(ClientRegistrationRepository clientRegistrationRepository, Environment env) {
		this.clientRegistrationRepository = clientRegistrationRepository;
		this.env = env;
	}
	
  
	@RequestMapping(value = "/oauth_login", method = RequestMethod.GET)
	public String Oauth2Login(ModelMap model) {
		
		Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
		Iterable<ClientRegistration> clientRegistrations = getRegisteredClients(clientRegistrationRepository);

		clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(),
				StringUtils.substring(env.getProperty("OAuth2AuthorizationRequestRedirectFilterInterceptorUri"), 1) + "/" + registration.getRegistrationId()));
		model.addAttribute("urls_oauth2", oauth2AuthenticationUrls);
		
		return "security/sign-in";
	}


	/**
	 * {@link ClientRegistration} is an interface and it does not have iterator() method as we have it in {@link InMemoryClientRegistrationRepository} which we have
	 * used in {@link DefaultPageController}. That approach is not good as we are bound to {@link InMemoryClientRegistrationRepository} only and later if we
	 * want to go with another {@link ClientRegistration}, we will get a problem. So This approach is better than that approach.
	 * @param clientRegistrationRepository
	 * @return Iterable&lt;ClientRegistration&gt;
	 */
	private Iterable<ClientRegistration> getRegisteredClients(ClientRegistrationRepository clientRegistrationRepository)
	{
		Iterable<ClientRegistration> clientRegistrations = null;
		ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
		if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
			clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
		}
		return clientRegistrations;
	}
}
