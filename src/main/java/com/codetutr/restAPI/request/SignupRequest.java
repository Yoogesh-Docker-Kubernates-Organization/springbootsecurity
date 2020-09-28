package com.codetutr.restAPI.request;

import javax.validation.constraints.NotBlank;

public class SignupRequest {
	
	@NotBlank(message = "username is mandatory")
	private String username;
	
	@NotBlank(message = "password is mandatory")
	private String password;
	
	@NotBlank(message = "FirstName is required")
	private String firstName;
	
	@NotBlank(message = "LastName is required")
	private String lastName;

	public SignupRequest() {
		super();
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
