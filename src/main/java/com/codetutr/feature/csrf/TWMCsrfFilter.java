package com.codetutr.feature.csrf;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TWMCsrfFilter extends OncePerRequestFilter{
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		/**
		 * Skipping CSRF check for Kubernetes deployment since ignoreCsrfForSpecificUrls is not working in kubernetes
		 */
		if(System.getProperty("envTarget").equals("prod")) {
			CsrfFilter.skipRequest(request);
		}
		
		filterChain.doFilter(request, response);
	}

}
