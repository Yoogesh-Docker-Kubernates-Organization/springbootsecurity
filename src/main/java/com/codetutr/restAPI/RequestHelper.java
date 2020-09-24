package com.codetutr.restAPI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.codetutr.restAPI.request.TWMRequestContextInfo;
import com.codetutr.validationHelper.LemonConstant;

public class RequestHelper {

	private RequestHelper() {
	}

	public static HttpServletRequest getCurrentRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	public static TWMRequestContextInfo getCurrentRequestBean(HttpServletRequest request) {
		if (request != null) {
			return (TWMRequestContextInfo) request.getAttribute(LemonConstant.TWM_REQUEST_CONTEXT_INFO_HEADER_NAME);
		}
		return null;
	}

	public static TWMRequestContextInfo getCurrentRequestBean() {
		TWMRequestContextInfo twmRequestContextInfo = new TWMRequestContextInfo();
		HttpServletRequest request = getCurrentRequest();
		if (request != null) {
			twmRequestContextInfo = (TWMRequestContextInfo) request
					.getAttribute(LemonConstant.TWM_REQUEST_CONTEXT_INFO_HEADER_NAME);
		}
		return twmRequestContextInfo;
	}

	public static String getTransactionId() {
		return getCurrentRequestBean().getTransactionId();
	}
}
