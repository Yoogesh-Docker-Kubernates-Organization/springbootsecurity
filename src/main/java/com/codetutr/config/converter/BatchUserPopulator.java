package com.codetutr.config.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.codetutr.config.batch.UserInput;
import com.codetutr.entity.User;
import com.codetutr.populatorHelper.ConverterName;
import com.codetutr.populatorHelper.Populator;
import com.codetutr.utility.UtilityHelper;

@ConverterName("batchUserConverter")
@Component
public class BatchUserPopulator implements Populator<UserInput, User> {
	
	private final Logger logger = LoggerFactory.getLogger(BatchUserPopulator.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void populate(UserInput source, User target) {
		logger.info("BatchUserPopulator is being executed.......");
		target.setUid(Long.parseLong(source.getId()));
		target.setUsername(source.getUsername());
		target.setPassword(passwordEncoder.encode(source.getPassword()));
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEnabled(true);
		target.setAuthorities(UtilityHelper.getUserAuthList(target));
	}
 
}