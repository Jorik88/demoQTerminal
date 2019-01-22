package com.example.alex.demoQTerminal.service;


import com.example.alex.demoQTerminal.enums.PaymentStatus;
import com.example.alex.demoQTerminal.model.Payment;
import com.example.alex.demoQTerminal.model.PaymentResponse;

public interface IPaymentDataService {
    Payment findOne(String id);
    boolean exists(String id);
    boolean isClose(String id);
    void save(Payment payment);
    void update(PaymentResponse paymentResponse);
    Payment update(String paymentId, PaymentStatus status);
    void updateFailedPayment(Payment payment);
}
