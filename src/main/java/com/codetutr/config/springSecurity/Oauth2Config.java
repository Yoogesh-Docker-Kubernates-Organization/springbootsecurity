package com.codetutr.config.springSecurity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@PropertySource("classpath:properties/oauth2/oauth2.properties")
public class Oauth2Config {
	
	private static List<String> clients = Arrays.asList("google", "facebook");
	
	@Autowired
	private Environment env;

	/**
	 * *************************
	 * initOauth2
	 * *************************
	 */
	
	 /**
	 * This is where you save clientId, Client Secret and other required information provided from google and FaceBook while registering as a client <p>
	 * <b>NOTE:</b> Client registration information is ultimately stored and owned by the associated Authorization Server. Therefore, this repository provides the 
	 * capability to store a sub-set copy of the <i>primary</i> client registration information externally from the Authorization Server. <p>
	 */
	@Bean
	public ClientRegistrationRepository getClientRegistrationRepository() {
		List<ClientRegistration> registrations = clients.stream()
				.map(c -> getRegistration(c))
				.filter(registration -> registration != null)
				.collect(Collectors.toList());
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
	
	
	
	
	
	/**
	 * *******************************
	 * oauth2RequestRepositoryAdapter
	 * ********************************
	 */
	
	/**
	 * This is where the URI for Leg1 call is Created in the form of {@link OAuth2AuthorizationRequest} object So that it can be used later for validation purpose
	 * and to initiate a Leg1 process. This {@link OAuth2AuthorizationRequest} is then saved into a {@link AuthorizationRequestRepository} ( <b>{@code if the GrantType is Authorization code}</b> )
	 * and will later be used by {@link OAuth2LoginAuthenticationFilter} as one of its member variable.<p>
	 * 
	 * This Class has its own style of creating a requestRedirectURI which is hard to customize. That's why we always need to provide a 
	 * {@code redirectUriTemplate = } {{@code baseUrl}}/{{@code action}}/{@code oauth2/code/}{{@code registrationId}} and it won't
     * work if you provide others. Please look into the {@code expandRedirectUri()} method about how the redirectUri is made. by the way if you 
     * really want to customize it, then you can have your own {@link OAuth2AuthorizationRequestResolver} implemented class. <p>
	 */
    @Bean
	private OAuth2AuthorizationRequestResolver getAuthorizationRequestResolver() {
		OAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
				getClientRegistrationRepository(), env.getProperty("OAuth2AuthorizationRequestRedirectFilterInterceptorUri"));
		return authorizationRequestResolver;
	}
	
	/**
	 * This is where the {@link OAuth2AuthorizationRequest} is saved. <p>
	 * 
	 * This is used by the {@link OAuth2AuthorizationRequestRedirectFilter} for persisting the Authorization Request before it initiates the authorization code grant flow. 
	 * As well, used by the {@link OAuth2LoginAuthenticationFilter} for resolving the associated Authorization Request when handling the callback of the Authorization Response.
	 */
    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
    
    
    
    
    
    
    
	/**
	 * ********************************
	 * oauth2ResponseRepositoryAdapter
	 * ********************************
	 */
    
	/**
	 *  This is where an information of Resource Owner after the successful Leg2 process is saved <p>
	 *  This is one of the member variable of {@link OAuth2AuthorizedClientRepository}. This {@link OAuth2AuthorizedClientRepository} is actually a member variable of
	 *  {@link OAuth2LoginAuthenticationFilter}. <p>
	 */
	@Bean
	public OAuth2AuthorizedClientService getAuthorizedClientService() {
	    return new InMemoryOAuth2AuthorizedClientService(getClientRegistrationRepository());
	}
	
    /**
     * This is actually the holder for {@link OAuth2AuthorizedClientService} where an information of Resource Owner after the successful Leg2 process is present  <p>
     * By default, {@link AuthenticatedPrincipalOAuth2AuthorizedClientRepository} is used by Spring-Security at {@link OAuth2LoginAuthenticationFilter} <p>
     */
	@Bean
	public OAuth2AuthorizedClientRepository getOAuth2AuthorizedClientRepository() {
		return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(getAuthorizedClientService());
	}
    
	
	
	private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(client + ".client-id");
        String clientSecret = env.getProperty(client + ".client-secret");
        String redirectUriTemplate = env.getProperty("redirectUriTemplate");
     
        if(StringUtils.isNotBlank(clientId)){
        	if (client.equals("google")) {
                return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                			.clientId(clientId)
                				.clientSecret(clientSecret)
                					.redirectUriTemplate(redirectUriTemplate)
                						.build();
            }
            if (client.equals("facebook")) {
                return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                			.clientId(clientId)
                				.clientSecret(clientSecret)
                					.redirectUriTemplate(redirectUriTemplate)
                						.build();
            }
        }
        return null;
    }
}
