package com.codetutr.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codetutr.entity.User;
import com.codetutr.validationHelper.ValidationHelper;

@Component
public class UserBeanValidator implements Validator
{

	@Override
	public boolean supports(Class<?> formType) 
	{
		boolean supported = false;
		
		if(User.class.equals(formType))
		{
			supported = true;
		}
		
		return supported;
	}
	
	

	@Override
	public void validate(Object formBean, Errors errors) 
	{
		// make a validation logic here
		User user = (User)formBean;
		
		String username = user.getUsername();
		if(!ValidationHelper.isValidRequired(username))
		{
	       errors.reject("validation.username.required");
		}
		
		String password = user.getPassword();
		if(!ValidationHelper.isValidRequired(password))
		{
	       errors.reject("validation.password.required");
		}
		
		String firstName = user.getFirstName();
		if(!ValidationHelper.isValidRequired(firstName))
		{
	       errors.reject("validation.firstName.required");
		}
		
		String lastName = user.getLastName();
		if(!ValidationHelper.isValidRequired(lastName))
		{
	       errors.reject("validation.lastName.required");
		}
	}

}
