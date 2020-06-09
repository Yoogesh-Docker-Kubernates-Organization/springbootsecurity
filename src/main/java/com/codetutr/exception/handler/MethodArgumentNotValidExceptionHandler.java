package com.codetutr.exception.handler;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.codetutr.exception.model.LemonFieldError;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class MethodArgumentNotValidExceptionHandler  extends AbstractValidationExceptionHandler<MethodArgumentNotValidException> {

	public MethodArgumentNotValidExceptionHandler() {
		super(MethodArgumentNotValidException.class);
	}
	
	@Override
	public Collection<LemonFieldError> getErrors(MethodArgumentNotValidException ex) {
		
		Collection<LemonFieldError> collection = new ArrayList<>();
		
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
			collection.add(LemonFieldError.of(error));
	    });
	    
		return collection;
	}
	
}
