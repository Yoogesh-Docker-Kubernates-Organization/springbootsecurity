package com.codetutr.stateMahine.interceptor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.codetutr.config.springStateMachine.StateMachineService;
import com.codetutr.dao.payment.IPaymentDao;
import com.codetutr.entity.Payment;
import com.codetutr.stateMahine.PaymentEvent;
import com.codetutr.stateMahine.PaymentState;

@Component
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent>{

	@Autowired 
	private IPaymentDao paymentDao;

	@Override
	public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message, Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine) 
	{
		Optional.ofNullable(message).ifPresent(msg -> {
			Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(StateMachineService.PAYMENT_ID_HEADER, -1L)))
			  .ifPresent(paymentId -> {
				  Payment payment = paymentDao.findPaymentById(paymentId);
				  payment.setState(state.getId());
				  paymentDao.saveorUpdatePayment(payment);
			  });
		});
	}
	
	
}
