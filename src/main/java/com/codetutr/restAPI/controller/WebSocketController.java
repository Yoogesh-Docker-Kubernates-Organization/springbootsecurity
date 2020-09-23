package com.codetutr.restAPI.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.feature.webSockets.SimpleMessage;
import com.codetutr.feature.webSockets.SimpleMessageDaoImpl;
import com.codetutr.validationHelper.LemonConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/websocket")
@Api(tags = {LemonConstant.SWAGGER_WEBSOCKET_DESCRIPTION})
public class WebSocketController {
	
	@Autowired
	private SimpleMessageDaoImpl simpleMessageDaoImpl;

	@PostMapping(value="/message")
	@ApiOperation(value="Save new message", notes="This url is used to save the information", response=SimpleMessage.class )
	public SimpleMessage saveMessage(@Valid @RequestBody SimpleMessage request){
		return simpleMessageDaoImpl.createSimpleMessage(request);
				
	}
	
	@GetMapping(value="/message")
	@ApiOperation(value="Get all messages", notes="This url is used to get all the messages in a database", response=ArrayList.class )
	public List<SimpleMessage> getAllMessage(){
		return simpleMessageDaoImpl.getAllMessages();
		
	}
	
	@GetMapping(value="/message/{username}")
	@ApiOperation(value="Get message by receivers username", notes="This url is used to get the Message informatio using the receivers username", response=ArrayList.class )
	public List<SimpleMessage> getMessageByReceiversUsername(@PathVariable("username") String username){
		return simpleMessageDaoImpl.getAllMessagesByReceiversUsername(username + "@gmail.com");
		
	}
}
