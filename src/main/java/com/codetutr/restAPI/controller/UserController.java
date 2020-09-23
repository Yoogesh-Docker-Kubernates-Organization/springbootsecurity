package com.codetutr.restAPI.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.entity.User;
import com.codetutr.services.UserService;
import com.codetutr.validationHelper.LemonConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/user")
@Api(tags = {LemonConstant.SWAGGER_USER_DESCRIPTION})
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Signup", notes="This url is used to create a user", response=User.class )
	public User signUp(HttpServletResponse response, @RequestBody User user){
		int i = 100/0;
		System.out.println(i);
		return new User(999L, "ysharma@gmail.com", "***", "Yoogesh", "Sharma", true, null);
	}
	
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Update user", notes="This url is used to update the user", response=User.class )
	public User updateUser(HttpServletResponse response, @RequestBody User user){
		return new User(999L, "ysharma@gmail.com", "***", "Yoogesh", "Sharma", true, null);
	}
	
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Delete user", notes="This url is used to update the user", response=User.class )
	public User deleteUser(HttpServletResponse response, @RequestBody User user){
		return new User(999L, "ysharma@gmail.com", "***", "Yoogesh", "Sharma", true, null);
	}
	
	@GetMapping(value="/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Get user by username", notes="This url is used to get the user information using the username", response=User.class )
	public User getUserByUsername(HttpServletRequest request, HttpServletResponse response, @PathVariable("username") String username){
		return userService.getUserByUserName(username + "@gmail.com");
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Get all Users", notes="This url is used to get all the users", response=User.class )
	public User getAllUsers(HttpServletRequest request, HttpServletResponse response, @PathVariable("username") String username){
		return userService.getUserByUserName(username + "@gmail.com");
	}

}