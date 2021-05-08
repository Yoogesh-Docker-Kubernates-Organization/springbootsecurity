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
import com.codetutr.restAPI.response.TWMResponse;
import com.codetutr.restAPI.response.TWMResponseFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/authentication")
@CrossOrigin(origins = "*") // This is optional here since we have already set it on CorsConfig.java. Meanwhile this will also be added to the list of allowed origins now which means all the origins are allowed for all the methods of this class.
public class AuthenticationController extends AbstractRestController {
	
	/**
	 * This will get the bean of {@link ProviderManager}
	 */
	@Autowired
	private AuthenticationManager authenticationManager;
		
	@Autowired
	private JwtService jwtService;
	
	@Operation(summary = "Signin", responses = {
			@ApiResponse(description = "This is used to login into an application", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthenticationResponse.class))),
			@ApiResponse(responseCode = "404", description = "Not found", content = @Content),
			@ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true)))
	})
	@PostMapping( produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public TWMResponse<AuthenticationResponse> login(HttpServletRequest request, HttpServletResponse response, 
			@Valid @RequestBody SigninRequest signInRequest) throws Exception {
		
		long guid;
		try { 
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
			guid = ((TWMUserDetails)authenticate.getPrincipal()).getUser().getUid();
			}
		catch (BadCredentialsException e) {throw new Exception("password invalid", e);}
		catch (Exception ex) {throw new Exception("username " + signInRequest.getUsername() + " not found", ex);}
		
		Map<String, Object> claimMap = new HashMap<>();
		claimMap.put("isApi", true);
		final String jwt = jwtService.generateToken("auth", signInRequest.getUsername(), claimMap);
		return TWMResponseFactory.getResponse(new AuthenticationResponse(jwt, guid), request);
	}
	
	@Operation(summary = "Signout", responses = {
			@ApiResponse(description = "This is used to signout from the application", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class))),
			@ApiResponse(responseCode = "404", description = "Not found", content = @Content),
			@ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true)))
	})
	@DeleteMapping(value = "/{guid}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public TWMResponse<Void> logout(HttpServletRequest request, HttpServletResponse response,
			@Valid @NotNull(message = "Authorization header should not be null") @RequestHeader(value = "Authorization", required = true) String Authorization,
			@Valid @Pattern(regexp = "^[0-9]*$", message = "GUID should a number.") @PathVariable Long guid,
			@Valid @Pattern(regexp = "^[a-zA-Z@.]*$", message = "username is not valid") @RequestParam (required = true) String username)
			throws Exception {
		
		User user;
		try {
			user = userService.getUser(guid);
			if(null == user) {
				throw new RuntimeException("User with GUID " + guid + " Could not found.");
			} else if(!user.getUsername().equals(username)) {
				throw new RuntimeException("Username " + username + " did not match with the guid " + guid);
			}
			return TWMResponseFactory.getResponse(null, request);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}

	}
}
