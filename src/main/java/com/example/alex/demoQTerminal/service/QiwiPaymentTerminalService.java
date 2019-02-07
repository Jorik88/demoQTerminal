package com.example.alex.demoQTerminal.service;

import com.example.alex.demoQTerminal.enums.PaymentStatus;
import com.example.alex.demoQTerminal.model.Payment;
import com.example.alex.demoQTerminal.model.PaymentResponse;
import com.example.alex.demoQTerminal.model.UserAccountInfo;
import com.example.alex.demoQTerminal.repository.PaymentRepository;
import com.example.alex.demoQTerminal.request.BasePaymentRequest;
import com.example.alex.demoQTerminal.response.CheckClientResponse;
import com.example.alex.demoQTerminal.response.Field;
import com.example.alex.demoQTerminal.response.PaymentClientResponse;
import com.example.alex.demoQTerminal.response.ResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Service
public class QiwiPaymentTerminalService {

    private static final String PAYMENT_ID_PREFIX = "QT_";
    private static final String PAYMENT_SYSTEM_NAME = "QiwiPayTerminal";
    private static final String PAYMENT_DATE_ATTRIBUTE_NAME = "prv-date";

    private IPaymentDataService paymentDataService;
    private PaymentRepository paymentRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public QiwiPaymentTerminalService(PaymentRepository paymentRepository) {
//        this.paymentDataService = paymentDataService;
        this.paymentRepository = paymentRepository;
    }

    public CheckClientResponse checkClientAndMakePayment(String command, String txn_id, String account, String sum, String ccy) {
        CheckClientResponse clientResponse = new CheckClientResponse();
        BasePaymentRequest baseRequest = new BasePaymentRequest(command, txn_id, account, sum, ccy);
        log.info("Handle check request, request={}", baseRequest);

        try {

            UserAccountInfo userAccountInfo = checkInternal(baseRequest);
            String paymentId = generatePaymentId(userAccountInfo.getId());

            clientResponse.setOsmpTxnId(baseRequest.getTxn_id());
            clientResponse.setPrvTxn(paymentId);
            clientResponse.setCurrency(baseRequest.getCcy());
            clientResponse.setSum(baseRequest.getSum());
            clientResponse.setComment("Ok");
            clientResponse.setResult(ResponseStatus.OK.getStatusCode());

            savePayment(clientResponse, paymentId);
            return clientResponse;

        } catch (Exception e) {
            log.warn("Error check user account, request={}", baseRequest);
            clientResponse.setResult(ResponseStatus.CHECK_ACCOUNT_ERROR.getStatusCode());
            clientResponse.setOsmpTxnId(baseRequest.getTxn_id());
            return clientResponse;
        }
    }

    public PaymentClientResponse confirmPayment(String command, String txn_id, String tnx_date, String account, String sum, String ccy) {
        PaymentClientResponse paymentClientResponse = new PaymentClientResponse();
        BasePaymentRequest baseRequest = new BasePaymentRequest(command, txn_id, tnx_date, account, sum, ccy);
        log.info("Handle payment request, request={}", baseRequest);

        try {

            Payment payment = checkPayment(baseRequest.getTxn_id(), baseRequest.getAccount(), baseRequest.getSum(), baseRequest.getCcy());

            paymentClientResponse.setCurrency(payment.getCurrency());
            paymentClientResponse.setOsmpTxnId(baseRequest.getTxn_id());
            paymentClientResponse.setPrvTxn(payment.getPaymentId());
            paymentClientResponse.setResult(ResponseStatus.OK.getStatusCode());
            paymentClientResponse.setSum(String.valueOf(payment.getAmount().doubleValue()));

            Payment updatePayment = updatePayment(payment, PaymentStatus.PROCESSED);
            paymentClientResponse.setField(Collections.singletonList(new Field(PAYMENT_DATE_ATTRIBUTE_NAME, new Date())));

            return paymentClientResponse;
        } catch (Exception e) {
            log.warn("Handle error confirm payment");
            paymentClientResponse.setResult(ResponseStatus.PAYMENT_ERROR.getStatusCode());
            paymentClientResponse.setOsmpTxnId(baseRequest.getTxn_id());
            return paymentClientResponse;
        }
    }

    private Payment checkPayment(String transactionId, String userId, String amount, String currency) {
        Payment byTransactionId = paymentRepository.findByTransactionId(transactionId);
        if (byTransactionId == null ||
                byTransactionId.getStatus() != PaymentStatus.PENDING ||
                !byTransactionId.getUserId().equals(userId) ||
                !byTransactionId.getAmount().equals(new BigDecimal(amount)) ||
                !byTransactionId.getCurrency().equals(currency)) {
            log.warn("Data in payment request incorrect");
            throw new IllegalArgumentException("Data in payment request incorrect");
        }
        return byTransactionId;
    }

    private void savePayment(CheckClientResponse clientResponse, String userId) {
        if (paymentDataService.exists(clientResponse.getPrvTxn())) {
            throw new IllegalArgumentException(String.format("The payment with paymentId=%s already exists", clientResponse.getPrvTxn()));
        } else {
            Payment payment = new Payment(clientResponse.getPrvTxn(), PAYMENT_SYSTEM_NAME, clientResponse.getCurrency(), new BigDecimal(clientResponse.getSum()), userId);
            payment.setTransactionId(clientResponse.getOsmpTxnId());
            paymentDataService.save(payment);
        }
    }

    private Payment updatePayment(Payment payment, PaymentStatus paymentStatus) {

        Payment savedPayment = paymentDataService.update(payment.getPaymentId(), paymentStatus);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentSystem(PAYMENT_SYSTEM_NAME);
        paymentResponse.setCurrency(savedPayment.getCurrency());
        paymentResponse.setPaymentId(savedPayment.getPaymentId());
        paymentResponse.setAmount(savedPayment.getAmount());
        paymentResponse.setUserId(savedPayment.getUserId());
        paymentResponse.setTransactionId(savedPayment.getTransactionId());
        paymentResponse.setStatus(paymentStatus);
//            paymentResponseProducer.pushPaymentResponse(paymentResponse);

        return payment;
    }

    private UserAccountInfo checkInternal(BasePaymentRequest clientRequest) {
        //toDo        Add checks for authorization service(find user by email)

        return new UserAccountInfo();
    }

    private String generatePaymentId(String userId) {
        return PAYMENT_ID_PREFIX + userId + "_" + System.currentTimeMillis();
    }

}
