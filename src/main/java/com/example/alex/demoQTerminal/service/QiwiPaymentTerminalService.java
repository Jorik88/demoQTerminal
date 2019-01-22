package com.example.alex.demoQTerminal.service;

import com.example.alex.demoQTerminal.enums.PaymentStatus;
import com.example.alex.demoQTerminal.model.Payment;
import com.example.alex.demoQTerminal.model.PaymentResponse;
import com.example.alex.demoQTerminal.model.UserAccountInfo;
import com.example.alex.demoQTerminal.repository.PaymentRepository;
import com.example.alex.demoQTerminal.request.BasePaymentRequest;
import com.example.alex.demoQTerminal.response.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

@Slf4j
@Service
public class QiwiPaymentTerminalService {

    private static final String PAYMENT_ID_PREFIX = "QT_";
    private static final String PAYMENT_SYSTEM_NAME = "QiwiPayTerminal";

    private IPaymentDataService paymentDataService;
    private PaymentRepository paymentRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public QiwiPaymentTerminalService(PaymentRepository paymentRepository) {
//        this.paymentDataService = paymentDataService;
        this.paymentRepository = paymentRepository;
    }

    public CheckClientResponse checkClientAndMakePayment(HttpServletRequest request) {
        log.info("Handle check request, request={}", request);
        CheckClientResponse clientResponse = new CheckClientResponse();

        try {

            BasePaymentRequest clientRequest = convertToBaseRequest(request);

            UserAccountInfo userAccountInfo = checkInternal(clientRequest);
            String paymentId = generatePaymentId(userAccountInfo.getId());

            clientResponse.setOsmpTxnId(clientRequest.getTxn_id());
            clientResponse.setPrvTxn(paymentId);
            clientResponse.setCurrency(clientRequest.getCcy());
            clientResponse.setSum(clientRequest.getSum());
            clientResponse.setComment("Ok");
            clientResponse.setResult(ResponseStatus.OK.getStatusCode());

            savePayment(clientResponse, paymentId);
            return clientResponse;

        } catch (Exception e) {
            log.warn("Error check user account, request={}", request);
            clientResponse.setResult(ResponseStatus.CHECK_ACCOUNT_ERROR.getStatusCode());
            clientResponse.setOsmpTxnId(request.getParameter("txn_id"));
            return clientResponse;
        }
    }

    private BasePaymentRequest convertToBaseRequest(HttpServletRequest request) {

        BasePaymentRequest baseRequest = new BasePaymentRequest();
        baseRequest.setTxn_id(request.getParameter("txn_id"));
        baseRequest.setAccount(request.getParameter("account"));
        baseRequest.setCcy(request.getParameter("ccy"));
        String command = request.getParameter("command");
        baseRequest.setCommand(command);
        baseRequest.setSum(request.getParameter("sum"));
        if (command.equals("pay")) {
            baseRequest.setTxn_date(request.getParameter("tnx_date"));
        }

        return baseRequest;
    }

    public PaymentClientResponse confirmPayment(HttpServletRequest request) {
        log.info("Handle payment request, request={}", request);
        PaymentClientResponse paymentClientResponse = new PaymentClientResponse();

        try {

            BasePaymentRequest paymentRequest = convertToBaseRequest(request);
            Payment payment = checkPayment(paymentRequest.getTxn_id(), paymentRequest.getAccount(), paymentRequest.getSum(), paymentRequest.getCcy());

            paymentClientResponse.setCurrency(paymentRequest.getCcy());
            paymentClientResponse.setOsmpTxnId(paymentRequest.getTxn_id());
            paymentClientResponse.setPrvTxn(payment.getPaymentId());
            paymentClientResponse.setResult(ResponseStatus.OK.getStatusCode());
            paymentClientResponse.setSum(paymentRequest.getSum());

            Payment updatePayment = updatePayment(payment, PaymentStatus.PROCESSED);
            paymentClientResponse.setFields(new Fields(Collections.singletonList(new Field("prv-date", getDate(updatePayment.getUpdateTimestamp())))));

            return paymentClientResponse;
        } catch (Exception e) {
            log.warn("Handle error confirm payment");
            paymentClientResponse.setResult(ResponseStatus.PAYMENT_ERROR.getStatusCode());
            paymentClientResponse.setOsmpTxnId(request.getParameter("txn_id"));
            return paymentClientResponse;
        }
    }

    private Payment checkPayment(String transactionId, String userId, String amount, String currency) {
        Payment byTransactionId = paymentRepository.findByTransactionId(transactionId);
        if (byTransactionId.getStatus() != PaymentStatus.PENDING ||
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
//        Add checks for authorization service(find user by email)

        return new UserAccountInfo();
    }

    private String generatePaymentId(String userId) {
        return PAYMENT_ID_PREFIX + userId + "_" + System.currentTimeMillis();
    }

    private LocalDateTime getDate(long seconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(1523261382698L), ZoneId.systemDefault()).withNano(0);
    }

}
