package com.codetutr.config.springStateMachine;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import com.codetutr.stateMahine.PaymentEvent;
import com.codetutr.stateMahine.PaymentState;

@EnableStateMachineFactory
public class AppConfig_StateMachine extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {
	
	@Autowired private Action<PaymentState, PaymentEvent> authAction;
	@Autowired private Action<PaymentState, PaymentEvent> authApproveAction;
	@Autowired private Action<PaymentState, PaymentEvent> authDeclineAction;
	@Autowired private Action<PaymentState, PaymentEvent> captureAction;
	@Autowired private Action<PaymentState, PaymentEvent> captureApproveAction;
	@Autowired private Action<PaymentState, PaymentEvent> captureDeclineAction;
	
	//Validator
	@Autowired private Guard<PaymentState, PaymentEvent> paymentIdGuard;

	@Override
	public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
		states.withStates()
		      /**
		       * Defining initial state
		       */
			  .initial(PaymentState.NEW)
			    
			    /**
			     * Loading all states into a state machine
			     */
			  	.states(EnumSet.allOf(PaymentState.class))
			  	
			  	    /**
			  	     * It defines when should we terminate the flow
			  	     */
			  	    .end(PaymentState.CAPTURE)
			  		.end(PaymentState.AUTH_ERROR)
			  		.end(PaymentState.CAPTURE_ERROR);
	}
	
	
	@Override
	public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
		transitions
				   /**
				    * Events may not necessarily cause the state change. for example as soon as the Authorization call is made,
				    * we are not changing the state here. The state will be the same i.e. NEW
				    * By the way if the paymentId is null, then it will never called because we have applied guard here
				    */		
					.withExternal().source(PaymentState.NEW).target(PaymentState.NEW).event(PaymentEvent.AUTH_INITIATE).action(authAction).guard(paymentIdGuard).and()

					/**
					 * When the event is AUTH_APPROVED then change the state to AUTH from NEW.
					 * However if the event is AUTH_DECLINE, then change to state to AUTH_ERROR
					 */		
					.withExternal().source(PaymentState.NEW).target(PaymentState.AUTH).event(PaymentEvent.AUTH_APPROVED).action(authApproveAction).and()
					.withExternal().source(PaymentState.NEW).target(PaymentState.AUTH_ERROR).event(PaymentEvent.AUTH_DECLINE).action(authDeclineAction).and()	
					
					
					//For Capture
					.withExternal().source(PaymentState.AUTH).target(PaymentState.AUTH).event(PaymentEvent.CAPTURE_INITIATE).action(captureAction).guard(paymentIdGuard).and()
					.withExternal().source(PaymentState.AUTH).target(PaymentState.CAPTURE).event(PaymentEvent.CAPTURE_APPROVED).action(captureApproveAction).and()
					.withExternal().source(PaymentState.AUTH).target(PaymentState.CAPTURE_ERROR).event(PaymentEvent.CAPTURE_DECLINE).action(captureDeclineAction);
	}
	
	@Override
	public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
		StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter<PaymentState, PaymentEvent>() {
			@Override
			public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
				System.out.println(String.format("State changed (from: %s, to: %s)",from.getId(), to.getId()));
			}
		};
		config.withConfiguration().listener(adapter);
	}
}
