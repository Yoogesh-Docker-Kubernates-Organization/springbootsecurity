package com.codetutr.config.exception;

import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import com.codetutr.exception.MultiErrorException;
import com.codetutr.exception.model.ExceptionIdMaker;

public class MyExceptionUtils {
	
	private final Logger logger = LoggerFactory.getLogger(MyExceptionUtils.class);

	private static MessageSource messageSource;
	private static Validator validator;
	private static ExceptionIdMaker exceptionIdMaker;
	
	public static final MultiErrorException NOT_FOUND_EXCEPTION = new MultiErrorException();
	
	public MyExceptionUtils(MessageSource messageSource, Validator validator, ExceptionIdMaker exceptionIdMaker) {
		MyExceptionUtils.messageSource = messageSource;
		MyExceptionUtils.validator = validator;
		MyExceptionUtils.exceptionIdMaker = exceptionIdMaker;
	}
	
	@PostConstruct
	public void postConstruct() {
		
		NOT_FOUND_EXCEPTION
			.httpStatus(HttpStatus.NOT_FOUND)
			.validate(false, "com.naturalprogrammer.spring.notFound");
		
		logger.info("NOT_FOUND_EXCEPTION built");		
	}

	
	/**
	 * Gets a message from messages.properties
	 */
	public static String getMessage(String messageKey, Object... args) {
		
		if (messageSource == null)
			return "ApplicationContext unavailable, probably unit test going on";
		
		// http://stackoverflow.com/questions/10792551/how-to-obtain-a-current-user-locale-from-spring-without-passing-it-as-a-paramete
		return messageSource.getMessage(messageKey, args,
				LocaleContextHolder.getLocale());
	}	

	
	/**
	 * Creates a MultiErrorException out of the given parameters
	 */
	public static MultiErrorException validate(
			boolean valid, String messageKey, Object... args) {
		
		return validateField(null, valid, messageKey, args);
	}

	
	/**
	 * Creates a MultiErrorException out of the given parameters
	 */
	public static MultiErrorException validateField(
			String fieldName, boolean valid, String messageKey, Object... args) {
		
		return new MultiErrorException().validateField(fieldName, valid, messageKey, args);
	}

	
	/**
	 * Creates a MultiErrorException out of the constraint violations in the given bean
	 */
	public static <T> MultiErrorException validateBean(String beanName, T bean, Class<?>... validationGroups) {
		
		return new MultiErrorException()
			.exceptionId(getExceptionId(new ConstraintViolationException(null)))
			.validationGroups(validationGroups)
			.validateBean(beanName, bean);
	}

	
	/**
	 * Throws 404 Error is the entity isn't found
	 */
	public static <T> void ensureFound(T entity) {
		
		MyExceptionUtils.validate(entity != null,
			"com.naturalprogrammer.spring.notFound")
			.httpStatus(HttpStatus.NOT_FOUND).go();
	}

	
	/**
	 * Supplys a 404 exception
	 */
	public static Supplier<MultiErrorException> notFoundSupplier() {
		
		return () -> NOT_FOUND_EXCEPTION;
	}
	

	public static String getExceptionId(Throwable ex) {
		
		Throwable root = getRootException(ex);
		return exceptionIdMaker.make(root);
	}
	
	
	private static Throwable getRootException(Throwable ex) {
		
		if (ex == null) return null;
			
		while(ex.getCause() != null)
			ex = ex.getCause();
		
		return ex;
	}

	public static Validator validator() {
		return validator;
	}

}
