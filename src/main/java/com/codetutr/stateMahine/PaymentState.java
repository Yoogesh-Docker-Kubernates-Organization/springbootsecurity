package com.codetutr.stateMahine;

public enum PaymentState {
	
	NEW,
	
	AUTH,
	AUTH_ERROR,
	
	CAPTURE,
	CAPTURE_ERROR
}
