package com.codetutr.config.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.codetutr.entity.User;
import com.codetutr.utility.UtilityHelper;

public class UserDataProcessor implements ItemProcessor<UserInput, User> {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@Override
	public User process(UserInput line) throws Exception {

		User user = new User();
		user.setUid(Long.parseLong(line.getId()));
		user.setUsername(line.getUsername());
		user.setPassword(line.getPassword());
		user.setFirstName(line.getFirstName());
		user.setLastName(line.getLastName());
		user.setEnabled(true);
		user.setAuthorities(UtilityHelper.getUserAuthList(user));
		
		return user;
	}

}
