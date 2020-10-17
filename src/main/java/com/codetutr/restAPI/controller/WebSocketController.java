package com.codetutr.restAPI.controller;

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

@RestController
@RequestMapping("/api/websocket")
public class WebSocketController {
	
	@Autowired
	private SimpleMessageDaoImpl simpleMessageDaoImpl;

	@PostMapping(value="/message")
	public SimpleMessage saveMessage(@Valid @RequestBody SimpleMessage request){
		return simpleMessageDaoImpl.createSimpleMessage(request);
				
	}
	
	@GetMapping(value="/message")
	public List<SimpleMessage> getAllMessage(){
		return simpleMessageDaoImpl.getAllMessages();
		
	}
	
	@GetMapping(value="/message/{username}")
	public List<SimpleMessage> getMessageByReceiversUsername(@PathVariable("username") String username){
		return simpleMessageDaoImpl.getAllMessagesByReceiversUsername(username + "@gmail.com");
		
	}
}
