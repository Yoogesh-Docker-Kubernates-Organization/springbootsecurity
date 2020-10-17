package com.codetutr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.config.springStateMachine.StateMachineService;
import com.codetutr.dao.payment.IPaymentDao;
import com.codetutr.entity.Payment;
import com.codetutr.stateMahine.PaymentEvent;
import com.codetutr.stateMahine.PaymentState;

@RestController
public class StateMachineController {

	@Autowired
	private StateMachineService stateMachineService;
	
	@Autowired 
	private IPaymentDao paymentDao;
	
	@RequestMapping(value = "/stateMachine", method = RequestMethod.GET)
	@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
	public String placeOrder() 
	{
		//Just Create a record into the database (With state NEW) before making a call to elavon
		Payment payment = stateMachineService.resetPayment();
		
		System.out.println("Initial: " + payment);
		
		//Authorize payment
		StateMachine<PaymentState, PaymentEvent> sm = stateMachineService.authorizePayment(payment.getId());
		
		if(sm.getState().getId().equals(PaymentState.AUTH)) {
		System.out.println("After Authorization: " + paymentDao.findPaymentById(payment.getId()));
		System.out.println("After Authorization [Obtaining state from StateMachine rather than the DB]: " + sm.getState().getId());
		}
		
		//Capture payment.By the way if the authorization failed, elavon will never be called (since it will be called only if the state is AUTH)
		StateMachine<PaymentState, PaymentEvent> smCapture = stateMachineService.capturePayment(payment.getId());
		
		if(smCapture.getState().getId().equals(PaymentState.CAPTURE)) {
			System.out.println("After Capture: " + paymentDao.findPaymentById(payment.getId()));
			System.out.println("After Capture [Obtaining state from StateMachine rather than the DB]: " + smCapture.getState().getId());
			
			return "AUTHORIZATION AND CAPTURE DONE SUCCESSFULLY. See console for more information........";
		}
		
		return "PAYMENT FAILED WITH REASON: <strong>" + smCapture.getState().getId() + "</strong>. See console for more information........";
	}
}
