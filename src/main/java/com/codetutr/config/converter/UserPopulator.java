package com.codetutr.config.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.codetutr.config.batch.UserInput;
import com.codetutr.entity.User;
import com.codetutr.populatorHelper.Populator;
import com.codetutr.utility.UtilityHelper;

@ConverterName("userConverter")
@Component
public class UserPopulator implements Populator<UserInput, User> {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void populate(UserInput source, User target) {
		target.setUid(Long.parseLong(source.getId()));
		target.setUsername(source.getUsername());
		target.setPassword(passwordEncoder.encode(source.getPassword()));
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEnabled(true);
		target.setAuthorities(UtilityHelper.getUserAuthList(target));
	}
 
}