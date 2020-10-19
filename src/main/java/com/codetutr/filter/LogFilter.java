package com.codetutr.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.codetutr.config.wrapper.TWMRequestWrapper;

//@WebFilter("/*")
public class LogFilter extends AbstractBaseFilter  {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException 
	{
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		 if(!StringUtils.contains(httpServletRequest.getRequestURL().toString(),"/websocket")) {
			 filterChain.doFilter(setCustomHeaders((HttpServletRequest) servletRequest), servletResponse);
		 }
		 else {
			 filterChain.doFilter(servletRequest, servletResponse);
		 }
		
	}

	private TWMRequestWrapper setCustomHeaders(HttpServletRequest request) {
		TWMRequestWrapper requestWrapper = new TWMRequestWrapper(request);
		setCustomHeader(request, requestWrapper);
		return requestWrapper;
	}

	private void setCustomHeader(HttpServletRequest request, TWMRequestWrapper requestWrapper) {
		if(null != request.getParameter("deploy")) {
			requestWrapper.setHeader("x-istio-header", request.getParameter("deploy"));
		}
	}
}
