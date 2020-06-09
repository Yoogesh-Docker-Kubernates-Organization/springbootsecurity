package com.codetutr.stateMahine.action;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.codetutr.stateMahine.PaymentEvent;
import com.codetutr.stateMahine.PaymentState;

@Component
public class CaptureDeclineAction implements Action<PaymentState, PaymentEvent> {

	@Override
	public void execute(StateContext<PaymentState, PaymentEvent> context) {
		System.out.println("CaptureDeclineAction is called.......Sending capture decline notification....");
	}

}
