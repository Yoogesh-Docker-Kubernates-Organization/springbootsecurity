package com.codetutr.restAPI.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.entity.User;
import com.codetutr.restAPI.request.SignupRequest;
import com.codetutr.restAPI.request.UpdateRequest;
import com.codetutr.restAPI.response.DeleteUserResponse;
import com.codetutr.restAPI.response.TWMResponse;
import com.codetutr.restAPI.response.TWMResponseFactory;
import com.codetutr.utility.UtilityHelper;
import com.codetutr.validationHelper.LemonConstant;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/user")
@Api(tags = {LemonConstant.SWAGGER_USER_DESCRIPTION})
public class UserController extends AbstractRestController {
	
	@PostMapping( produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value="Signup", notes="This url is used to create a user", response=User.class )
	public TWMResponse<User> signUp(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody SignupRequest signupRequest){
		
		if(userService.ismoreUsernameExists(signupRequest.getUsername())){
			throw new RuntimeException("Username " + signupRequest.getUsername() + " already exists.");
		}
		else {
			User user = new User();
			user.setUsername(signupRequest.getUsername());
			user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
			user.setFirstName(signupRequest.getFirstName());
			user.setLastName(signupRequest.getLastName());
			user.setEnabled(true);
			user.setAuthorities(UtilityHelper.getUserAuthList(user));
			User updatedUser = userService.createUser(user);
			updatedUser.setPassword(signupRequest.getPassword());
			return TWMResponseFactory.getResponse(updatedUser, request);
		}
	}

	@PatchMapping(value = "/{guid}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value="Update user", notes="This url is used to update the user", response=User.class )
	public TWMResponse<User> updateUser(HttpServletRequest request, HttpServletResponse response, 
			@Valid @Pattern(regexp = "^[0-9]*$", message = "GUID should a number.") @PathVariable Long guid, 
			@RequestBody UpdateRequest updateRequest){
		
		User user;
		
		try {
			user = userService.getUser(guid);
			if(null == user) {
				throw new RuntimeException("User with GUID " + guid + " Could not found.");
			} 

			if(StringUtils.isNotBlank(updateRequest.getUsername()))
				user.setUsername(updateRequest.getUsername());
			if(StringUtils.isNotBlank(updateRequest.getPassword()))
				user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
			if(StringUtils.isNotBlank(updateRequest.getFirstName()))
				user.setFirstName(updateRequest.getFirstName());
			if(StringUtils.isNotBlank(updateRequest.getLastName()))
				user.setLastName(updateRequest.getLastName());
			return TWMResponseFactory.getResponse(userService.updateUser(user), request);
			
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}
	
	@DeleteMapping(value = "/{guid}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value="Delete user", notes="This url is used to delete the user", response=DeleteUserResponse.class )
	public TWMResponse<DeleteUserResponse> logout(HttpServletRequest request, HttpServletResponse response,
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
			return TWMResponseFactory.getResponse(new DeleteUserResponse(userService.deleteUser(guid)), request);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Get user by username", notes="This url is used to get the user information using the username", response=User.class )
	public TWMResponse<User> getUserByUsername(HttpServletRequest request, HttpServletResponse response, 
			@Valid @Pattern(regexp = "^[0-9]*$", message = "Guid is not valid") @RequestParam (required = false) Long guid,
			@Valid @Pattern(regexp = "^[a-zA-Z@.]*$", message = "username is not valid") @RequestParam (required = false) String username){
		
		User user;
		try {
			if(null != guid) {
				user = userService.getUser(guid);
			}
			else if(StringUtils.isNotBlank(username)) {
				user = userService.getUserByUserName(username);
			}
			else {
				throw new RuntimeException("Eithere guid or username must be provided.");
			}

			if(StringUtils.isNotBlank(username) && user == null) {
				throw new RuntimeException("Username is invalid.");
			} else if(null != guid && user == null) {
				throw new RuntimeException("guid is invalid.");
			}
		}
		catch (NullPointerException ex) {
			throw new RuntimeException(username + " is not found in the Database." + ex);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return TWMResponseFactory.getResponse(user, request);
	}
	
	
	@GetMapping(value="/all", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value="Get all Users", notes="This url is used to get all the users", response=User.class )
	public TWMResponse<List<User>> getAllUsers(HttpServletRequest request) {
		return TWMResponseFactory.getResponse(userService.getAllUsers(), request);
	}


}
