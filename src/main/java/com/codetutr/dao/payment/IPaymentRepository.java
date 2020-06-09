package com.codetutr.dao.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codetutr.entity.Payment;

public interface IPaymentRepository extends JpaRepository<Payment, Long>{
}
