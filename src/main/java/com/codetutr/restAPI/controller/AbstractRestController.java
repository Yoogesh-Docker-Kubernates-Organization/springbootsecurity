package com.codetutr.restAPI.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.codetutr.restAPI.request.TWMRequestContextInfo;
import com.codetutr.services.RuleEngineService;
import com.codetutr.services.UserService;

public abstract class AbstractRestController {
	
	private final Logger logger = LoggerFactory.getLogger(AbstractRestController.class);
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected RuleEngineService ruleEngineService;
	
	@Autowired
	protected PasswordEncoder passwordEncoder;
	
	protected void logAdditionalInfo(TWMRequestContextInfo requestContextInfo) {
		String clientIp = StringUtils.isBlank(requestContextInfo.getIpAddress()) ? "Unknown" : requestContextInfo.getIpAddress();
		String browserAgent = StringUtils.isBlank(requestContextInfo.getBrowserAgent()) ? "Unknown" : requestContextInfo.getIpAddress();
		String transactionId = StringUtils.isBlank(requestContextInfo.getTransactionId()) ? "Unknown" : requestContextInfo.getIpAddress();
		logger.info("Request received:  [transactionId= " + transactionId + "][cientip = " + clientIp + "],[browserAgent = " + browserAgent + "]");
	}
	
	protected static Map<String, String> getAllRequestHeaders(HttpServletRequest request) {
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
