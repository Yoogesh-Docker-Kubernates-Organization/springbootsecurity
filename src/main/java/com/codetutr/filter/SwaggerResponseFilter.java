package com.codetutr.filter;

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

import com.codetutr.config.wrapper.TWMResponseWrapper;

public class SwaggerResponseFilter extends AbstractBaseFilter {
	
	private final Logger logger = LoggerFactory.getLogger(SwaggerResponseFilter.class);
	
	private static final String APPLICATION_JSON = "application/json";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		   HttpServletRequest servletRequest = (HttpServletRequest) request;
		   TWMResponseWrapper responseWrapper;
		   logger.info("Forwarded Request via {}", servletRequest.getRequestURL().toString()); 
		   
		   if(StringUtils.contains(servletRequest.getRequestURL().toString(), "/forwardRequestViaFilter")) {
			   responseWrapper = new TWMResponseWrapper((HttpServletResponse) response);
			   
			   filterChain.doFilter(request, responseWrapper); 
			   
			   if (response.getContentType() != null && response.getContentType().contains("application/json")) {
				   String originalResponse = responseWrapper.getCaptureAsString();
				   logger.info("Original Response: {}", originalResponse);
				   
				   //Do the manipulation according to your needs
				   //String modififiedResponse = StringUtils.chop(StringUtils.remove(new Gson().fromJson(originalResponse, Object.class).toString(), "{value="));
				   String modifiedResponse = originalResponse.toUpperCase();
				   logger.info("Modified Response: {}", modifiedResponse);
				   
				   response.setContentLength(modifiedResponse .length());
				   response.getWriter().write(modifiedResponse);
			   }
		   } else if(StringUtils.contains(servletRequest.getRequestURI(), "/v3/api-docs")) {
			   responseWrapper = new TWMResponseWrapper((HttpServletResponse) response);
			   filterChain.doFilter(request, responseWrapper);
				
				if (response.getContentType() != null && response.getContentType().contains(APPLICATION_JSON))
				{
					response.setContentType(APPLICATION_JSON);
					response.getWriter().write(responseWrapper.getCaptureAsString());
				}
		   } else if (StringUtils.contains(servletRequest.getRequestURI(), "/swagger-ui/swagger-ui.css")) {

				responseWrapper = new TWMResponseWrapper((HttpServletResponse) response);
				filterChain.doFilter(request, responseWrapper);
				
				 String result = responseWrapper.getCaptureAsString();
				 String modifiedResponse = StringUtils
						.replace(result, "swagger-ui .scheme-container{margin", "swagger-ui .scheme-container{display:none;margin")
						.replace(".swagger-ui .info{margin:50px 0}", ".swagger-ui .info{margin-top:20px;margin-bottom:10px;}");
				response.getWriter().write(modifiedResponse);
			} else {
			   filterChain.doFilter(request, response); 
		   }
	}

}
