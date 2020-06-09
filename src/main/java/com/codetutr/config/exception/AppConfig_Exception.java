package com.codetutr.config.exception;

import java.util.List;

import javax.validation.Validator;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.codetutr.exception.controllerAdvice.DefaultExceptionHandlerControllerAdvice;
import com.codetutr.exception.controllerAdvice.LemonErrorAttributes;
import com.codetutr.exception.controllerAdvice.LemonErrorController;
import com.codetutr.exception.handler.AbstractExceptionHandler;
import com.codetutr.exception.model.ErrorResponseComposer;
import com.codetutr.exception.model.ExceptionIdMaker;

@Configuration
@AutoConfigureBefore({ValidationAutoConfiguration.class})
public class AppConfig_Exception {
	
	/**
	 * This must be configure here.. othere it will give error at compile time if you put it at AppConfig_Mvc.class
	 */
	@Bean(name="messageSource")
	public ResourceBundleMessageSource resourceBundleMessageSource()
	{
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
	@Bean
	@ConditionalOnMissingBean(ErrorResponseComposer.class)
	public <T extends Throwable> ErrorResponseComposer<T> errorResponseComposer(List<AbstractExceptionHandler<T>> handlers) {
		return new ErrorResponseComposer<T>(handlers);
	}

	@Bean
	@ConditionalOnMissingBean(ExceptionIdMaker.class)
	public ExceptionIdMaker getExceptionIdMaker() {
		
        return ex -> {
        	
        	if (ex == null)
        		return null;
        	
        	return ex.getClass().getSimpleName();
        };
	}

	
	@Bean
	public MyExceptionUtils lexUtils() {
		return new MyExceptionUtils(resourceBundleMessageSource(), getValidator(resourceBundleMessageSource()), getExceptionIdMaker());
	}
	
	
	/**
	 * Merge ValidationMessages.properties into messages.properties
	 */	
    @Bean
	@ConditionalOnMissingBean(Validator.class)
    public Validator getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }
    
    
	/**
	 * Configures DefaultExceptionHandlerControllerAdvice if missing
	 */	
	@Bean
	@ConditionalOnMissingBean(DefaultExceptionHandlerControllerAdvice.class)
	public <T extends Throwable>
	DefaultExceptionHandlerControllerAdvice<T> defaultExceptionHandlerControllerAdvice(
    		ErrorResponseComposer<T> errorResponseComposer) {
		return new DefaultExceptionHandlerControllerAdvice<T>(errorResponseComposer);
	}
	
	/**
	 * Configures an Error Attributes if missing
	 */	
	@Bean
	@ConditionalOnMissingBean(ErrorAttributes.class)
	public <T extends Throwable>
	ErrorAttributes errorAttributes(ErrorResponseComposer<T> errorResponseComposer) {
		return new LemonErrorAttributes<T>(errorResponseComposer);
	}
    
	@Bean
	@ConditionalOnMissingBean(ErrorController.class)
	public ErrorController errorController(ErrorAttributes errorAttributes,
			ServerProperties serverProperties,
			List<ErrorViewResolver> errorViewResolvers) {
		return new LemonErrorController(errorAttributes, serverProperties, errorViewResolvers);	
	}	

}
