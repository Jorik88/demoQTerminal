package com.example.alex.demoQTerminal.controller;

import com.example.alex.demoQTerminal.response.CheckClientResponse;
import com.example.alex.demoQTerminal.response.PaymentClientResponse;
import com.example.alex.demoQTerminal.service.QiwiPaymentTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QiwiController {

    private QiwiPaymentTerminalService service;

    @Autowired
    public QiwiController(QiwiPaymentTerminalService service) {
        this.service = service;
    }

    @RequestMapping(value = "/api/input/payment/terminal/qiwi/check", produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<CheckClientResponse> checkAccount(@RequestParam String command,
                                                            @RequestParam String txn_id,
                                                            @RequestParam String account,
                                                            @RequestParam String sum,
                                                            @RequestParam String ccy) {

        return ResponseEntity.ok(service.checkClientAndMakePayment(command, txn_id ,account, sum, ccy));
    }

    @RequestMapping(value = "/api/input/payment/terminal/qiwi/pay", produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<PaymentClientResponse> confirmPayment(@RequestParam String command,
                                                                @RequestParam String txn_id,
                                                                @RequestParam String txn_date,
                                                                @RequestParam String account,
                                                                @RequestParam String sum,
                                                                @RequestParam String ccy) {

       return ResponseEntity.ok(service.confirmPayment(command, txn_id, txn_date, account, sum, ccy));
//        return ResponseEntity.ok(new PaymentClientResponse("code", "own_code", "32.3", "USD",
//                ResponseStatus.OK.getStatusCode(), "OK",
//                new Fields(Collections.singletonList(new Field("prv-date", LocalDateTime.now().withNano(0))))));
    }

}
