package com.fkhrayef.capstone3.Repository;

import com.fkhrayef.capstone3.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Payment findPaymentById(Integer id);

    // Find payment by Moyasar payment ID
    Payment findByMoyasarPaymentId(String moyasarPaymentId);

}
