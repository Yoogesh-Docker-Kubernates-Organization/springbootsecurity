package com.codetutr.feature.jwt;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Profile({"Mock", "SpringJdbc", "SpringEmJPA", "Hibernate" })
@Service
@PropertySource({"classpath:properties/custom/custom-${envTarget:local}.properties"})
public class JwtServiceImpl implements JwtService {
	
	@Value("${custom.jwt.secret.key}")
    private String secretKey;
	
	@Value("${custom.jwt.token.expiration.minute}")
    private String expireMinutes;
	
	public static final String AUTH_AUDIENCE = "auth";
    
    @Override
	public String generateToken(String audience, String subject, Map<String, Object> claims) {
        return Jwts
        		.builder()
        		    .setAudience(audience)
        		    .setSubject(subject)
        			.setClaims(claims)
        			.setIssuedAt(new Date(System.currentTimeMillis()))
        			.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expireMinutes) * 60 * 1000))
        			.signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
    
    @Override
    public Boolean isTokenExpired(String token) {
    	Date dateOfExpiration = extractClaim(token, Claims::getExpiration);
    	return  dateOfExpiration.before(new Date());
    }
    
    @Override
    public Boolean validateToken(String token, String audience, String subject) {
    	final String username = extractUsername(token);
    	return (username.equals(subject) && !isTokenExpired(token));
    }
    
    @Override
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
}