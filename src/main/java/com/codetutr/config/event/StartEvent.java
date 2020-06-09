package com.codetutr.config.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.debug.DebugFilter;
import org.springframework.stereotype.Component;

import com.codetutr.config.springSecurity.AppConfig_Security;
import com.codetutr.services.UserService;

@Component
public class StartEvent
{
	private final Logger logger = LoggerFactory.getLogger(StartEvent.class);

	
	@Autowired
	private Environment env;
	
	@Autowired
	UserService userService;
	
	@Autowired
	@Qualifier("springSecurityFilterChain")
	private Filter springSecurityFilterChain;
	
	@Autowired
	private InMemoryClientRegistrationRepository clientRegistrationRepository;
	
	@EventListener
	public void afterApplicationReady(ApplicationReadyEvent event) {
		if(!userService.ismoreUsernameExists("admin@gmail.com")) {
			logger.debug("Previous database does not exist. Creating a fresh one.");
			userService.initiateDatabase();
		}
		printFilterChain();
		getActiveOAuth2Providers(clientRegistrationRepository.iterator());
	}
	

	private void printFilterChain() {
		if(springSecurityFilterChain instanceof DebugFilter) {
			System.out.println("[*** DebugFilter in Spring-security is currently Set to On. Please get the active filters in a Filterchain from a Log file ***]");
		}
		else {
			getActiveFiltersFromFilterChain();
		}
	}
	
	/**
	 * The enableDebugMode() must be commented out at {@link AppConfig_Security} to make this work <p>
	 */
	private void getActiveFiltersFromFilterChain() {
		System.out.println("With a " + Arrays.asList(env.getActiveProfiles()) +" environment, Following filters are active in the application: ");
	    FilterChainProxy filterChainProxy = (FilterChainProxy) springSecurityFilterChain;
	    List<SecurityFilterChain> list = filterChainProxy.getFilterChains();
	    list.stream()
	      .flatMap(chain -> chain.getFilters().stream()) 
	      .forEach(filter -> System.out.println(filter.getClass()));
	}
	
	private void getActiveOAuth2Providers(Iterator<ClientRegistration> iterator) {
		List<ClientRegistration> providers = new ArrayList<>();
		while(iterator.hasNext()){
			providers.add(iterator.next());
		}
		
		System.out.println("*********************************************************************************************");
		System.out.println("                       Currently Registred OAuth2 venders : " + providers.size());
		System.out.println("*********************************************************************************************\n");
		
		for (ClientRegistration clientRegistration : providers) {
			
			System.out.println("***********");
			System.out.println(clientRegistration.getClientName() + ":");
			System.out.println("*********** \n");
			
						System.out.println("     *******************");
						System.out.println("     client information:");
						System.out.println("     *******************");
						System.out.println("                          registrationId = " + clientRegistration.getRegistrationId());
						System.out.println("                          clientName = " + clientRegistration.getClientName());
						System.out.println("                          clientId = " + clientRegistration.getClientId());
						System.out.println("                          clientSecret = " + clientRegistration.getClientSecret());
						System.out.println("                          clientAuthenticationMethod = " + clientRegistration.getClientAuthenticationMethod().getValue());
						System.out.println("                          authorizationGrantType = " + clientRegistration.getAuthorizationGrantType().getValue());
						System.out.println("                          redirectUriTemplate = " + clientRegistration.getRedirectUriTemplate());
						System.out.println("                          redirectUriTemplate to be register at " + clientRegistration.getClientName() + "-client-API side as Authorized-redirect-URI = http://localhost:8080/SpringSecurity-AnnotationApproach/login/oauth2/code/" + clientRegistration.getRegistrationId());
						System.out.println("                          scopes = " + clientRegistration.getScopes());

						System.out.println("     *******************");
						System.out.println("     providerDetails:");
						System.out.println("     *******************");			
						System.out.println("                          AuthorizationUri = " + clientRegistration.getProviderDetails().getAuthorizationUri());			
						System.out.println("                          JwkSetUri = " + clientRegistration.getProviderDetails().getJwkSetUri());			
						System.out.println("                          TokenUri = " + clientRegistration.getProviderDetails().getTokenUri());			
						System.out.println("                          ConfigurationMetadata = " + clientRegistration.getProviderDetails().getConfigurationMetadata());	
						
						System.out.println("     *******************");
						System.out.println("     UserInfoEndpoint:");
						System.out.println("     *******************");			
						System.out.println("                          URI = " + clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri());			
						System.out.println("                          UserNameAttributeName = " + clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());							
						System.out.println("                          AuthenticationMethod = " + clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod().getValue());							
			
			System.out.println("\n\n");
		}
	}
	
}
