package com.example.alex.demoQTerminal.controller;

import com.example.alex.demoQTerminal.response.CheckClientResponse;
import com.example.alex.demoQTerminal.response.PaymentClientResponse;
import com.example.alex.demoQTerminal.service.QiwiPaymentTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class QiwiController {

    private QiwiPaymentTerminalService service;

    @Autowired
    public QiwiController(QiwiPaymentTerminalService service) {
        this.service = service;
    }

    @RequestMapping(value = "/api/input/payment/terminal/qiwi/check", produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<CheckClientResponse> checkAccount(HttpServletRequest request) {

        return ResponseEntity.ok(service.checkClientAndMakePayment(request));
    }

    @RequestMapping(value = "/api/input/payment/terminal/qiwi/pay", produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<PaymentClientResponse> confirmPayment(HttpServletRequest request) {

       return ResponseEntity.ok(service.confirmPayment(request));
//        return ResponseEntity.ok(new PaymentClientResponse("code", "own_code", "32.3", "USD",
//                ResponseStatus.OK.getStatusCode(), "OK",
//                new Fields(Collections.singletonList(new Field("prv-date", LocalDateTime.now().withNano(0))))));
    }


    @RequestMapping(value = "/check", produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<CheckClientResponse> doCheckTry(HttpServletRequest request) {

        System.out.println();
        return ResponseEntity.ok().build();
    }
}
