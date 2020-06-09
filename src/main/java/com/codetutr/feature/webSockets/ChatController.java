package com.codetutr.feature.webSockets;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.codetutr.validationHelper.LemonConstant;

@Controller
public class ChatController {
	
	@Autowired
	//@Qualifier("simpMessagingTemplate")
	private SimpMessagingTemplate brokerMessagingTemplate;
	
	@Autowired
	private SimpleMessageDaoImpl simpleMessageDaoImpl;
	
	
	@MessageMapping("/message/register")
	@SendTo(LemonConstant.WEB_SOCKET_TOPIC_NAME + "/pubic")
	public SimpleMessage register(@Payload SimpleMessage payload, SimpMessageHeaderAccessor headeAccessor) {
		headeAccessor.getSessionAttributes().put("username", payload.getSenderUsername());
		return payload;
	}

	@MessageMapping("/message/send/{receiver}")
	public void send(@Payload SimpleMessage payload, @DestinationVariable String receiver) {
		
		if(null != payload.getMessage()) {
			
			//Saving message into repository (Make sure this method is async)
			simpleMessageDaoImpl.createSimpleMessageWithAsyncCall(payload);
			
			//Send a message to the receiver
			brokerMessagingTemplate.convertAndSendToUser(payload.getRecieverUsername(), LemonConstant.WEB_SOCKET_QUEUE_NAME + "/" + payload.getSenderUsername(), payload);
			
			//Send a message back to the sender as an acknowledgement
			payload.setIsreplied(false);
			brokerMessagingTemplate.convertAndSendToUser(payload.getSenderUsername(), LemonConstant.WEB_SOCKET_QUEUE_NAME + "/" + payload.getRecieverUsername(), payload);
		}
		else {
			String sendTo = receiver + "@gmail.com";
			ArrayList<SimpleMessage> messages = simpleMessageDaoImpl.getAllMessagesByReceiversUsername(sendTo);
			
			if(!messages.isEmpty()) {
				brokerMessagingTemplate.convertAndSendToUser(sendTo, LemonConstant.WEB_SOCKET_QUEUE_NAME + "/" + LemonConstant.WEB_SOCKET_NOTIFICATION, messages);
			}
		}
	}
}
