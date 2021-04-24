package com.codetutr.config.batch;

import org.springframework.batch.item.ItemProcessor;

import com.codetutr.entity.User;
import com.codetutr.populatorHelper.Converter;

public class UserDataProcessor implements ItemProcessor<UserInput, User> {

	private Converter<UserInput, User> userConverter;
	
	public UserDataProcessor(Converter<UserInput, User> userConverter) {
		this.userConverter = userConverter;
	}


	@Override
	public User process(UserInput line) throws Exception {
		return userConverter.convert(line);
	}

}
