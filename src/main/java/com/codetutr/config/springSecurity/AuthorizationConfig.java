package com.codetutr.config.springSecurity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import com.codetutr.security.handler.CustomPermissionEvaluator;

public class AuthorizationConfig {

	@Bean
    public AccessDecisionManager getAccessDecisionManager()
    {
    	List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
    	voters.add(getwebExpressionVoter());
    	return new AffirmativeBased(voters);
    }
	
	@Bean
	public WebExpressionVoter getwebExpressionVoter() {
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		webExpressionVoter.setExpressionHandler(getExpressionHandler());
		return webExpressionVoter;
	}
	
	@Bean
	public SecurityExpressionHandler<FilterInvocation> getExpressionHandler() {
		DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
		expressionHandler.setTrustResolver(getTrustResolver());
		expressionHandler.setPermissionEvaluator(getPermissionEvaluator()); 
		expressionHandler.setRoleHierarchy(getRoleHierarchy());
		return expressionHandler;
	}
	
	/**
	 * This {@link AuthenticationTrustResolverImpl} bean is provided out of the box. So we don't need to define here until and unless we have our own 
	 * {@link AuthenticationTrustResolver} implementation class. It has been declared here just for Learning purpose only.</p>
	 */
	@Bean
	public AuthenticationTrustResolver getTrustResolver() {
		AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
		return trustResolver;
	}
	
	/**
	 *  This is required for permission kind of expressions like {@link @PreAuthorize("hasPermission(#user, 'edit')")}.
	 *  This calls the {@link hasPermission()} method of {@link PermissionEvaluator} which was originally called from 
	 *  {@link hasPermission()} method of {@link SecurityExpressionRoot} which is the main implementation root class of 
	 *  {@link SecurityExpressionOperations} interface. The out of the box {@link PermissionEvaluator} implementation class provided to us is 
	 *  {@link DenyAllPermissionEvaluator} which always returns false.
	 *  So that we need to have our own permissionEvaluator with {@link hasPermission()} method overridden on it.<p>
	 */
	@Bean
	public PermissionEvaluator getPermissionEvaluator() {
		PermissionEvaluator permissionEvaluator = new CustomPermissionEvaluator();
		return permissionEvaluator;
	}
	
	/**
	 *  This is required for Role kind of expressions like {@link @PreAuthorize("hasAuthority('ROLE_USER')")}.
	 *  This {@link hasAuthority()} method can be found at {@link SecurityExpressionOperations} interface which is the main interface that
	 *  our root class {@link SecurityExpressionRoot} implements<p>
	 */
	@Bean
	public RoleHierarchy getRoleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy("ROLE_DBA > ROLE_ADMIN \n ROLE_DBA > ROLE_USER \n ROLE_ADMIN > ROLE_USER");
		return roleHierarchy;
	}

}
