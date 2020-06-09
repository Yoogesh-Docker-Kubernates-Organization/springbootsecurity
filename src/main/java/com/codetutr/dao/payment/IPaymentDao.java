package com.codetutr.dao.payment;

import com.codetutr.entity.Payment;

public interface IPaymentDao {

	Payment saveorUpdatePayment(Payment payment);
	
	Payment findPaymentById(Long paymentId);
}