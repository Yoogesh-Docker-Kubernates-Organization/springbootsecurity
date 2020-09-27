package com.codetutr.restAPI.response;

public class AuthenticationResponse {
	
	private final String jwt;
	private final Long guid;
	
	public AuthenticationResponse(String jwt, Long guid) {
		this.jwt = jwt;
		this.guid = guid;
	}
	
	public String getJwt() {
		return jwt;
	}

	public Long getGuid() {
		return guid;
	}
}
