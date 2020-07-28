package com.codetutr.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	@ApiOperation(value="Save the user", notes="This url is used to save the user", response=User.class )
	public User saveUser(HttpServletResponse response, @RequestBody User user){
		int i = 100/0;
		System.out.println(i);
		return new User(999L, "ysharma@gmail.com", "***", "Yoogesh", "Sharma", true, null);
	}
	
	@GetMapping(value="/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Get user by username", notes="This url is used to get the user informatio using the username", response=User.class )
	public User getUserByUsername(HttpServletRequest request, HttpServletResponse response, @PathVariable("username") String username){
		return userService.getUserByUserName(username + "@gmail.com");
	}

}
