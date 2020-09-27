package com.codetutr.restAPI.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.config.userDetails.TWMUserDetails;
import com.codetutr.entity.User;
import com.codetutr.feature.jwt.JwtService;
import com.codetutr.restAPI.request.SigninRequest;
import com.codetutr.restAPI.response.AuthenticationResponse;
import com.codetutr.restAPI.response.LogoutResponse;
import com.codetutr.restAPI.response.TWMResponse;
import com.codetutr.restAPI.response.TWMResponseFactory;
import com.codetutr.services.UserService;
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
	
	@Autowired
	UserService userService;
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Authenticate the user", notes="This url is used to get the JWT Token back which can be used for further call.", response=AuthenticationResponse.class )
	public TWMResponse<AuthenticationResponse> login(HttpServletRequest request, HttpServletResponse response, 
			@Valid @RequestBody SigninRequest signInRequest) throws Exception {
		
		long guid;
		try { 
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
			guid = ((TWMUserDetails)authenticate.getPrincipal()).getUser().getUid();
			}
		catch (BadCredentialsException e) {throw new Exception("password invalid", e);}
		catch (Exception ex) {throw new Exception("username not found", ex);}
		
		Map<String, Object> claimMap = new HashMap<>();
		claimMap.put("isApi", true);
		final String jwt = jwtService.generateToken("auth", signInRequest.getUsername(), claimMap);
		return TWMResponseFactory.getResponse(new AuthenticationResponse(jwt, guid), request);
	}
	
	@DeleteMapping(value = "/{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Logout", notes = "This url is used to logging out from the application", response = AuthenticationResponse.class)
	public TWMResponse<LogoutResponse> logout(HttpServletRequest request, HttpServletResponse response,
			@Valid @NotNull(message = "Authorization header should not be null") @RequestHeader(value = "Authorization", required = true) String Authorization,
			@Valid @Pattern(regexp = "^[0-9]*$", message = "Guid should a number.") @PathVariable String guid,
			@Valid @Pattern(regexp = "^[a-zA-Z@.]*$", message = "username is not valid") @RequestParam (required = true) String username)
			throws Exception {
		return TWMResponseFactory.getResponse(new LogoutResponse(true), request);
	}
}
