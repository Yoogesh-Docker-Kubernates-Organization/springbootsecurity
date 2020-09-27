package com.codetutr.feature.jwt;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.codetutr.validationHelper.ValidationHelper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

@Profile("SpringDataJPA")
@Service
@PropertySource({ "classpath:properties/custom/custom-${envTarget:local}.properties" })
public class NimbusServiceImpl implements JwtService {

	private DirectEncrypter encrypter;
	private JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);
	private ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor;
	private String expireMinutes;
		
	public static final String AUTH_AUDIENCE = "auth";
	
	
	@Autowired
	public NimbusServiceImpl(Environment env) throws KeyLengthException 
	{ 
		byte[] secretKey = env.getProperty("custom.jwt.secret.key").getBytes();
		expireMinutes = env.getProperty("custom.jwt.token.expiration.minute");
		encrypter = new DirectEncrypter(secretKey);
		jwtProcessor = new DefaultJWTProcessor<SimpleSecurityContext>();

		// The JWE key source
		JWKSource<SimpleSecurityContext> jweKeySource = new ImmutableSecret<SimpleSecurityContext>(secretKey);

		// Configure a key selector to handle the decryption phase
		JWEKeySelector<SimpleSecurityContext> jweKeySelector = new JWEDecryptionKeySelector<SimpleSecurityContext>(
				JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256, jweKeySource);

		jwtProcessor.setJWEKeySelector(jweKeySelector);
	}

	
	@Override
	public String generateToken(String audience, String subject, Map<String, Object> claimMap) {
		
		JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
		builder
				.issueTime(new Date())
				.expirationTime(new Date(System.currentTimeMillis() + Long.parseLong(expireMinutes) * 60 * 1000))
				.audience(audience)
				.subject(subject);

		claimMap.forEach(builder::claim);
		
		JWTClaimsSet claims = builder.build();
		Payload payload = new Payload(claims.toJSONObject());

		// Create the JWE object and encrypt it
		JWEObject jweObject = new JWEObject(header, payload);

		try {
			jweObject.encrypt(encrypter);
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		}
		
		// Serialize to compact JOSE form...
		return jweObject.serialize();
	}

	@Override
	public Boolean isTokenExpired(String token) {
		JWTClaimsSet claims = parseToken(token);

		long expirationTime = claims.getExpirationTime().getTime();
		long currentTime = System.currentTimeMillis();
		
		return expirationTime >= currentTime;
	}
	
	public Boolean invalidateToken(String token) {
		JWTClaimsSet claims = parseToken(token);
		
		long expirationTime = claims.getExpirationTime().getTime();
		long currentTime = System.currentTimeMillis();
		
		return expirationTime >= currentTime;
	}

	@Override
	public Boolean validateToken(String token, String audience, String subject) {
		JWTClaimsSet claims = parseToken(token);
		
//		ValidationHelper.ensureCredentials(audience != null && claims.getAudience().contains(audience),
//				"com.naturalprogrammer.spring.wrong.audience");
		
		long expirationTime = claims.getExpirationTime().getTime();
		long currentTime = System.currentTimeMillis();

		ValidationHelper.ensureCredentials(expirationTime >= currentTime, "com.naturalprogrammer.spring.expiredToken");
		
		return true;
	}

	@Override
	public String extractUsername(String token) {
		JWTClaimsSet claims = parseToken(token);
		return claims.getSubject();
	}

	/**
	 * extract Only token
	 */
	public JWTClaimsSet parseToken(String token) {

		try {

			return jwtProcessor.process(token, null);

		} catch (ParseException | BadJOSEException | JOSEException e) {

			throw new BadCredentialsException(e.getMessage());
		}
	}

	
	/**
	 * 
	 * Extract token and claim both
	 */
	public <T> T extractClaim(String token, String claim) {

		JWTClaimsSet claims = parseToken(token);
		return (T) claims.getClaim(claim);
	}


}
