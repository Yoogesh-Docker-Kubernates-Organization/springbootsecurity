package com.codetutr.restAPI.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.drools.Server;
import com.codetutr.restAPI.response.TWMResponse;
import com.codetutr.restAPI.response.TWMResponseFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/rule")
public class DroolsController extends AbstractRestController {

	@GetMapping(value = "/yms", produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Get all Users", responses = {
			@ApiResponse(description = "This url is used to get all the users", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Server.class))),
			@ApiResponse(responseCode = "404", description = "Not found", content = @Content),
			@ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
	public TWMResponse<List<Server>> getAllUsers(HttpServletRequest request) {

		Server s1 = new Server("rhel7", 2, 1024, 2048);
		ruleEngineService.addServerFacts(s1);
		Server s2 = new Server("rhel8", 2, 2048, 4096);
		ruleEngineService.addServerFacts(s2);

		List<Server> response = new ArrayList<>();
		response.add(s1);
		response.add(s2);

		return TWMResponseFactory.getResponse(response, request);
	}

}
