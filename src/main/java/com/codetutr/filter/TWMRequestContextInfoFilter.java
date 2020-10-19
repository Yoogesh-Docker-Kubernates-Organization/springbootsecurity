package com.codetutr.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codetutr.config.logging.TrackingLogger;
import com.codetutr.config.wrapper.TWMRequestWrapper;
import com.codetutr.restAPI.TransportAPI;
import com.codetutr.restAPI.request.TWMRequestContextInfo;
import com.codetutr.validationHelper.LemonConstant;
import com.itextpdf.xmp.impl.Base64;

public class TWMRequestContextInfoFilter extends AbstractBaseFilter implements Filter {
	
	 private final Logger logger = LoggerFactory.getLogger(TWMRequestContextInfoFilter.class);

	public TWMRequestContextInfoFilter() {
	}

	@Override
	public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse, FilterChain chain)
			throws IOException, ServletException {

		
		HttpServletRequest httpServletRequest = (HttpServletRequest) baseRequest;
		 if(!StringUtils.contains(httpServletRequest.getRequestURL().toString(),"/websocket")) {
				HttpServletRequest request = (HttpServletRequest) baseRequest;
				HttpServletResponse response = (HttpServletResponse) baseResponse;
				TWMRequestWrapper wrapper = setHeader(request);
				chain.doFilter(wrapper, response);
		 }
		 else {
			 chain.doFilter(baseRequest, baseResponse);
		 }



	}

	private TWMRequestWrapper setHeader(HttpServletRequest request) {
		TWMRequestContextInfo requestContextInfo = new TWMRequestContextInfo();
		String requestContextInfoHeader = request.getHeader(LemonConstant.TWM_REQUEST_CONTEXT_INFO_HEADER_NAME);
		
		if (StringUtils.isNotBlank(requestContextInfoHeader)) {
			try {
				requestContextInfo = TransportAPI.decode(Base64.decode(requestContextInfoHeader), TWMRequestContextInfo.class);
				if(StringUtils.isNotBlank(requestContextInfo.getTransactionId())) {
					logger.info("Request From: {} || TransactionId: {}", requestContextInfo.getTriggeredById(), requestContextInfo.getTransactionId());
				}
			} catch (Exception e) {
				throw new RuntimeException("Could not parse the requestContext header: ", e);
			}
		}
		
		if(StringUtils.isBlank(requestContextInfo.getTransactionId()))
				requestContextInfo.setTransactionId(TrackingLogger.getTransactionIdforCurrentRequest());
		if(StringUtils.isBlank(requestContextInfo.getIpAddress()))
				requestContextInfo.setIpAddress(getClientIpAddress(request));
		if(StringUtils.isBlank(requestContextInfo.getBrowserAgent()))
				requestContextInfo.setBrowserAgent(StringUtils.trimToNull(request.getHeader("User-Agent")));
		if(StringUtils.isBlank(requestContextInfo.getTriggeredById()))
			requestContextInfo.setTriggeredById("springBootSecurity");

		
		TWMRequestWrapper wrapper = new TWMRequestWrapper(request);
		wrapper.setAttribute(LemonConstant.TWM_REQUEST_CONTEXT_INFO_HEADER_NAME, requestContextInfo);
	    return wrapper;
	}

	public static String getClientIpAddress(HttpServletRequest request) {
		String clientIpAddress = null;
		String forwardedForIpAddressCollection = StringUtils.trimToNull(request.getHeader("X-Forwarded-For"));

		if (forwardedForIpAddressCollection == null) {
			clientIpAddress = request.getRemoteAddr();
		} else {
			clientIpAddress = forwardedForIpAddressCollection.split(",")[0];
			clientIpAddress = StringUtils.substringBeforeLast(clientIpAddress, ":");
		}
		return StringUtils.trimToNull(clientIpAddress);
	}
	


}
