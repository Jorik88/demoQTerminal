package com.example.alex.demoQTerminal.repository;

import com.example.alex.demoQTerminal.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    Payment findByTransactionId(String transactionId);
}
