package com.codetutr.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.codetutr.config.logging.TrackingLogger;
import com.codetutr.config.wrapper.TWMRequestWrapper;
import com.codetutr.restAPI.TransportAPI;
import com.codetutr.restAPI.request.TWMRequestContextInfo;
import com.codetutr.validationHelper.LemonConstant;
import com.itextpdf.xmp.impl.Base64;

public class TWMRequestContextInfoFilter extends AbstractBaseFilter implements Filter {

	public TWMRequestContextInfoFilter() {
	}

	@Override
	public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) baseRequest;
		HttpServletResponse response = (HttpServletResponse) baseResponse;

		TWMRequestWrapper wrapper = setHeader(request);
		chain.doFilter(wrapper, response);

	}

	private TWMRequestWrapper setHeader(HttpServletRequest request) {
		
		TWMRequestContextInfo requestContextInfo = new TWMRequestContextInfo();
		String requestContextInfoHeader = request.getHeader(LemonConstant.TWM_REQUEST_CONTEXT_INFO_HEADER_NAME);
		
		if (StringUtils.isBlank(requestContextInfoHeader)) {
	    	requestContextInfo.setTransactionId(TrackingLogger.getTransactionIdforCurrentRequest());
	    	requestContextInfo.setIpAddress(getClientIpAddress(request));
	    	requestContextInfo.setBrowserAgent(StringUtils.trimToNull(request.getHeader("User-Agent")));
	    	requestContextInfo.setTriggeredById("springBootSecurity");
	    	
	    } else {
	    	try {
				requestContextInfo = TransportAPI.decode(Base64.decode(requestContextInfoHeader), TWMRequestContextInfo.class);
			} catch (Exception e) {
				throw new RuntimeException("Could not parse the requestContext header: ", e);
			}
	    }
		
		TWMRequestWrapper wrapper = new TWMRequestWrapper(request);
		wrapper.setAttribute(LemonConstant.TWM_REQUEST_CONTEXT_INFO_HEADER_NAME, requestContextInfo);
		System.out.println("here is...." + requestContextInfo);
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
	
	private static Map<String, String> getAllRequestHeaders(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}

}
