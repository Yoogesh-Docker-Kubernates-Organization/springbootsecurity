package com.codetutr.feature.jwt;

import java.util.Map;

public interface JwtService {

	String generateToken(String audience, String subject, Map<String, Object> claims);
		
	Boolean isTokenExpired(String token);
	
	Boolean validateToken(String token, String audience, String subject);

	String extractUsername(String token);
}