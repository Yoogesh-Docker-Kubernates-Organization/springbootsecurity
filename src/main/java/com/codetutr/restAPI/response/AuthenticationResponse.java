package com.codetutr.restAPI.response;

public class AuthenticationResponse {
	
	private final String jwt;
	private final Long uid;
	
	public AuthenticationResponse(String jwt, Long uid) {
		this.jwt = jwt;
		this.uid = uid;
	}
	
	public String getJwt() {
		return jwt;
	}

	public Long getUid() {
		return uid;
	}
}
