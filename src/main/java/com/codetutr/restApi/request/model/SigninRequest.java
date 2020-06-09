package com.codetutr.restApi.request.model;

import javax.validation.constraints.NotBlank;

public class SigninRequest {
	
	@NotBlank(message = "username is mandatory")
	private String username;
	
	@NotBlank(message = "password is mandatory")
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
