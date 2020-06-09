package com.codetutr.config.springStateMachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import com.codetutr.dao.payment.IPaymentDao;
import com.codetutr.entity.Payment;
import com.codetutr.stateMahine.PaymentEvent;
import com.codetutr.stateMahine.PaymentState;
import com.codetutr.stateMahine.interceptor.PaymentStateChangeInterceptor;

@Service
public class StateMachineService {

	public static final String PAYMENT_ID_HEADER = "Payment_id";
	
	@Autowired private IPaymentDao paymentDao;
	@Autowired private StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;
	@Autowired private PaymentStateChangeInterceptor paymentStateChangeInterceptor;
	
	public Payment resetPayment() {
		Payment payment = new Payment();
		payment.setState(PaymentState.NEW);
		return paymentDao.saveorUpdatePayment(payment);
	}

	public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
		return doProcessState(paymentId, PaymentEvent.AUTH_INITIATE);
	}

	public StateMachine<PaymentState, PaymentEvent> capturePayment(Long paymentId) {
		return doProcessState(paymentId, PaymentEvent.CAPTURE_INITIATE);
	}

	/**
	 * Process states
	 */
	private StateMachine<PaymentState, PaymentEvent> doProcessState(Long paymentId, PaymentEvent captureDecline) {
		StateMachine<PaymentState, PaymentEvent> sm= build(paymentId);
		sendEvent(paymentId, sm, captureDecline);
		return sm;
	}
	
	private StateMachine<PaymentState, PaymentEvent> build(Long paymentId){
		Payment payment = paymentDao.findPaymentById(paymentId);
		StateMachine<PaymentState, PaymentEvent> sm = stateMachineFactory.getStateMachine(Long.toString(payment.getId()));
		
		sm.stop();
		
		sm.getStateMachineAccessor().doWithAllRegions(sma -> {
			sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
			sma.resetStateMachine(new DefaultStateMachineContext<PaymentState, PaymentEvent>(payment.getState(), null, null, null));
		});
		
		sm.start();
		
		return sm;
	}

	private void sendEvent(Long id, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event) {
		Message<PaymentEvent> msg = MessageBuilder.withPayload(event).setHeader(PAYMENT_ID_HEADER, id).build();
		sm.sendEvent(msg);
	}
}
