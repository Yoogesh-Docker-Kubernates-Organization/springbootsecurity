package com.codetutr.stateMahine.action;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.codetutr.stateMahine.PaymentEvent;
import com.codetutr.stateMahine.PaymentState;

@Component
public class AuthDeclineAction implements Action<PaymentState, PaymentEvent>{

	@Override
	public void execute(StateContext<PaymentState, PaymentEvent> context) {
		System.out.println("AuthDeclineAction is called....... Sending auth decline notification......");
	}

}
