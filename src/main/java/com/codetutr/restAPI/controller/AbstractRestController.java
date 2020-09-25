package com.codetutr.restAPI.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codetutr.restAPI.TransportAPI;
import com.codetutr.restAPI.request.TWMRequestContextInfo;
import com.codetutr.validationHelper.LemonConstant;


public abstract class AbstractRestController {
	
	private final Logger logger = LoggerFactory.getLogger(AbstractRestController.class);
	
	protected void logAdditionalInfo(TWMRequestContextInfo requestContextInfo) {
		String clientIp = StringUtils.isBlank(requestContextInfo.getIpAddress()) ? "Unknown" : requestContextInfo.getIpAddress();
		String browserAgent = StringUtils.isBlank(requestContextInfo.getBrowserAgent()) ? "Unknown" : requestContextInfo.getIpAddress();
		String transactionId = StringUtils.isBlank(requestContextInfo.getTransactionId()) ? "Unknown" : requestContextInfo.getIpAddress();
		logger.info("Request received:  [transactionId= " + transactionId + "][cientip = " + clientIp + "],[browserAgent = " + browserAgent + "]");
	}

}
