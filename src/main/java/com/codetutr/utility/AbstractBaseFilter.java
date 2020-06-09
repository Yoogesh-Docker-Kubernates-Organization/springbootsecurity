package com.codetutr.utility;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.codetutr.config.logging.Log;

public abstract class AbstractBaseFilter implements Filter 
{
	public void init(FilterConfig filterConfig) throws ServletException 
	{
		Log log = new Log();
	    log.configure(filterConfig.getServletContext().getRealPath("/WEB-INF"));
	}

	public abstract void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException;

	public void destroy() { }

}