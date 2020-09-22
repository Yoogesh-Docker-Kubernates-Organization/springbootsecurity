package com.codetutr.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.feature.jwt.JwtService;
import com.codetutr.restApi.request.model.AuthenticationResponse;
import com.codetutr.restApi.request.model.SigninRequest;
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
	public AuthenticationResponse login(HttpServletResponse response, @Valid @RequestBody SigninRequest request) throws Exception {
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		}
		catch (BadCredentialsException e) {
			throw new Exception("password invalid", e);
		}
		catch (Exception ex) {
			throw new Exception("username not found", ex);
		}
	
		
		Map<String, Object> claimMap = new HashMap<>();
		claimMap.put("isApi", true);
		
		final String jwt = jwtService.generateToken("auth", request.getUsername(), claimMap);

		return new AuthenticationResponse(jwt);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Authenticate the user", notes="This url is used to get the JWT Token back which can be used for further call.", response=AuthenticationResponse.class )
	public AuthenticationResponse login11(HttpServletResponse response) throws Exception {
		
		return new AuthenticationResponse("YouAreWelcome");
	}

}
