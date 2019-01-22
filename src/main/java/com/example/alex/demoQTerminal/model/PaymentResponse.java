package com.example.alex.demoQTerminal.model;

import com.example.alex.demoQTerminal.enums.PaymentStatus;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentResponse {
    private String paymentSystem;
    private String currency;
    private String paymentId;
    private BigDecimal amount;
    private String userId;
    private String transactionId;
    private String userAccount;
    private PaymentStatus status;
    private String lastFourNumeralsOfCard;
    private String cardToken;
    private String senderCardMask;
}
