package com.codetutr.exception.handler;

import java.util.Collection;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.codetutr.exception.model.LemonFieldError;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class WebExchangeBindExceptionHandler extends AbstractValidationExceptionHandler<WebExchangeBindException> {

	public WebExchangeBindExceptionHandler() {
		
		super(WebExchangeBindException.class);
		log.info("Created");
	}
	
	@Override
	public Collection<LemonFieldError> getErrors(WebExchangeBindException ex) {
		return LemonFieldError.getErrors(ex);
	}
}
