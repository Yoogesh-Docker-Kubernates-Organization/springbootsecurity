package com.codetutr.restAPI.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.codetutr.validation.annotation.ValidPassword;

public class SigninRequest {
	
	@NotBlank(message = "username is mandatory")
	@NotNull(message = "username is mandatory")
	private String username;
	
	@NotBlank(message = "password is mandatory")
	@ValidPassword(message = "Password length should not be greater than 4")
	private String password;
	
	public SigninRequest() {
		super();
	}

	public SigninRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
