package com.codetutr.config.swagger;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codetutr.utility.AbstractBaseFilter;

public class SwaggerResponseFilter extends AbstractBaseFilter {
	
	private final Logger logger = LoggerFactory.getLogger(SwaggerResponseFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		   HttpServletRequest servletRequest = (HttpServletRequest) request;
		   logger.info("Forwarded Request via {}", servletRequest.getRequestURL().toString());
		   
		   if(StringUtils.contains(servletRequest.getRequestURL().toString(), "/forwardRequestViaFilter")) {
			   HtmlResponseWrapper capturingResponseWrapper = new HtmlResponseWrapper((HttpServletResponse) response);
			   
			   filterChain.doFilter(request, capturingResponseWrapper); 
			   
			   if (response.getContentType() != null && response.getContentType().contains("application/json")) {
				   String originalResponse = capturingResponseWrapper.getCaptureAsString();
				   logger.info("Original Response: {}", originalResponse);
				   
				   //Do the manipulation according to your needs
				   //String modififiedResponse = StringUtils.chop(StringUtils.remove(new Gson().fromJson(originalResponse, Object.class).toString(), "{value="));
				   String modifiedResponse = originalResponse.toUpperCase();
				   logger.info("Modified Response: {}", modifiedResponse);
				   
				   response.setContentLength(modifiedResponse .length());
				   response.getWriter().write(modifiedResponse);
			   }
		   }
		   else {
			   filterChain.doFilter(request, response); 
		   }
	}

}
