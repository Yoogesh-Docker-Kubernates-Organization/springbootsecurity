package com.codetutr.restAPI.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.feature.jwt.JwtService;
import com.codetutr.restAPI.request.SigninRequest;
import com.codetutr.restAPI.response.AuthenticationResponse;
import com.codetutr.validationHelper.LemonConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/authentication")
@CrossOrigin(origins = "*") // This is optional here since we have already set it on CorsConfig.java. Meanwhile this will also be added to the list of allowed origins now which means all the origins are allowed for all the methods of this class.
@Api(tags = {LemonConstant.SWAGGER_AUTHENTICATION_DESCRIPTION})
public class AuthenticationController {
	
	/**
	 * This will get the bean of {@link ProviderManager}
	 */
	@Autowired
	private AuthenticationManager authenticationManager;
		
	@Autowired
	private JwtService jwtService;
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Authenticate the user", notes="This url is used to get the JWT Token back which can be used for further call.", response=AuthenticationResponse.class )
	public AuthenticationResponse login(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody SigninRequest signInRequest) throws Exception {
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
		}
		catch (BadCredentialsException e) {
			throw new Exception("password invalid", e);
		}
		catch (Exception ex) {
			throw new Exception("username not found", ex);
		}
	
		
		Map<String, Object> claimMap = new HashMap<>();
		claimMap.put("isApi", true);
		
		final String jwt = jwtService.generateToken("auth", signInRequest.getUsername(), claimMap);

		return new AuthenticationResponse(jwt);
	}
	
	@DeleteMapping(value="/{username}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Logout", notes="This url is used to logging out from the application", response=AuthenticationResponse.class )
	public AuthenticationResponse logout(HttpServletResponse response, @Valid @RequestBody SigninRequest request) throws Exception {
		
		return new AuthenticationResponse("");
	}
	
	private Map<String, String> getReqeustHeaderInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}
}
