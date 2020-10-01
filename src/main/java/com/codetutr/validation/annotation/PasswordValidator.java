package com.codetutr.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		//Null will be handled by @NotNull annotation
		if(StringUtils.isBlank(password)){
			return true;
		}
		return password.length() < 5;
	}
	
	@Override
	public void initialize(ValidPassword constraintAnnotation) {
		//leave default
	}

}
