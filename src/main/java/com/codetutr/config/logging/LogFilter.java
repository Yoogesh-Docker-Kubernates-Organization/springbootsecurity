package com.codetutr.config.logging;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.codetutr.utility.AbstractBaseFilter;

//@WebFilter("/*")
public class LogFilter extends AbstractBaseFilter  {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException 
	{
		filterChain.doFilter(setCustomHeaders((HttpServletRequest) servletRequest), servletResponse);
	}

	private TWMHttpRequestWrapper setCustomHeaders(HttpServletRequest request) {
		TWMHttpRequestWrapper requestWrapper = new TWMHttpRequestWrapper(request);
		setCustomHeader(request, requestWrapper);
		return requestWrapper;
	}

	private void setCustomHeader(HttpServletRequest request, TWMHttpRequestWrapper requestWrapper) {
		if(null != request.getParameter("deploy")) {
			requestWrapper.setHeader("x-istio-header", request.getParameter("deploy"));
		}
	}
}
