package com.codetutr.stateMahine.action;

import java.util.Random;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.codetutr.config.springStateMachine.StateMachineService;
import com.codetutr.stateMahine.PaymentEvent;
import com.codetutr.stateMahine.PaymentState;

@Component
public class CaptureAction implements Action<PaymentState, PaymentEvent> {

	@Override
	public void execute(StateContext<PaymentState, PaymentEvent> context) {
		System.out.println("CaptureAction is called.......");
		
		System.out.println("Elavon call for Capture initiated.....");
		
		//make an elavon call for capture.......
		boolean elavonResponse = new Random().nextInt(10) < 5;
		
		if(elavonResponse) {
			System.out.println("Elavon call for Capture is successful.....");
			
			
			//update the stateMachine with CAPTURE_APPROVED
			context.getStateMachine()
					 .sendEvent(MessageBuilder.withPayload(PaymentEvent.CAPTURE_APPROVED)
							 .setHeader(StateMachineService.PAYMENT_ID_HEADER, context.getMessageHeader(StateMachineService.PAYMENT_ID_HEADER))
							 	.build());
		}else {
			System.out.println("Elavon call for capture failed.....");
			
			//update the stateMachine with CAPTURE_DECLINE
			context.getStateMachine()
			 .sendEvent(MessageBuilder.withPayload(PaymentEvent.CAPTURE_DECLINE)
					 .setHeader(StateMachineService.PAYMENT_ID_HEADER, context.getMessageHeader(StateMachineService.PAYMENT_ID_HEADER))
					 	.build());
		}
	
	}

}
