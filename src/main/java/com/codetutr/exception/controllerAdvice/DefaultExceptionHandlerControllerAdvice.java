package com.codetutr.exception.controllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.codetutr.exception.model.ErrorResponse;
import com.codetutr.exception.model.ErrorResponseComposer;


/**
 * Handles exceptions thrown from in controllers or inner routines
 */
@RestControllerAdvice
public class DefaultExceptionHandlerControllerAdvice<T extends Throwable> {
	
	private final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandlerControllerAdvice.class);

	/**
	 * Component that actually builds the error response
	 */
	private ErrorResponseComposer<T> errorResponseComposer;
	
    public DefaultExceptionHandlerControllerAdvice(ErrorResponseComposer<T> errorResponseComposer) {
		this.errorResponseComposer = errorResponseComposer;
	}


	/**
     * Handles exceptions
     */
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleException(T ex) throws T {

    	ErrorResponse errorResponse = errorResponseComposer.compose(ex).orElseThrow(() -> ex);
    	
    	// Propogate up if message or status is null 
    	if (errorResponse.incomplete())
    		throw ex;
    	
    	logger.warn("Handling exception", ex);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }
}
