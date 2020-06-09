package com.codetutr.stateMahine.guard;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import com.codetutr.config.springStateMachine.StateMachineService;
import com.codetutr.stateMahine.PaymentEvent;
import com.codetutr.stateMahine.PaymentState;

@Component
public class PaymentIdGuard implements Guard<PaymentState, PaymentEvent> {

	@Override
	public boolean evaluate(StateContext<PaymentState, PaymentEvent> context) {
		Long paymentId = (Long) context.getMessageHeader(StateMachineService.PAYMENT_ID_HEADER);
		if(null == paymentId) {
			System.out.println("Error: PaymentId appares to be null. PaymentId should not be null to proceed.....");
			return false;
		}
		return true;
	}
	
	/*
	public Guard<PaymentState, PaymentEvent> paymentIdValidator(){
		return context ->{
			return context.getMessageHeader(StateMachineService.PAYMENT_ID_HEADER) != null;
		};
	}
	*/
}
