package com.codetutr.dao.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.codetutr.entity.Payment;

@Repository
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
public class SpringDataJPAPaymentDaoImpl implements IPaymentDao {

	@Autowired
	private IPaymentRepository paymentRepository;

	@Override
	public Payment saveorUpdatePayment(Payment payment) {
		return paymentRepository.save(payment);
	}

	@Override
	public Payment findPaymentById(Long paymentId) {
		return paymentRepository.getOne(paymentId);
	}
	
}
