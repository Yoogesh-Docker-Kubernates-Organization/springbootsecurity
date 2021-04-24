package com.codetutr.config.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.codetutr.entity.User;
import com.codetutr.populatorHelper.ConversionException;
import com.codetutr.populatorHelper.ConverterName;
import com.codetutr.populatorHelper.Populator;

@ConverterName("userConverter")
@Component
public class UserPopulator implements Populator<User, User> {

	private final Logger logger = LoggerFactory.getLogger(UserPopulator.class);
	
	@Override
	public void populate(User source, User target) throws ConversionException {
		logger.info("UserPopulator is being executed.......");
		target.setUid(source.getUid());
		target.setUsername(source.getUsername());
		target.setPassword(source.getPassword());
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEnabled(source.isEnabled());
		target.setAuthorities(source.getAuthorities());
		
	}

}
