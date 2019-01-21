package com.example.alex.demoQTerminal.controller;

import com.example.alex.demoQTerminal.response.CheckClientResponse;
import com.example.alex.demoQTerminal.service.QiwiPaymentTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/check")
    public ResponseEntity<CheckClientResponse> doCheck(@RequestParam String command,
                                                       @RequestParam String txn_id,
                                                       @RequestParam String account,
                                                       @RequestParam String sum,
                                                       @RequestParam String ccy) {

        return ResponseEntity.ok(service.checkClient(command, txn_id, account, sum, ccy));
    }

    @RequestMapping(value = "/pay")
    public void doPay(@RequestParam String command,
                      @RequestParam String txn_id,
                      @RequestParam String txn_date,
                      @RequestParam String account,
                      @RequestParam String sum,
                      @RequestParam String ccy) {

        service.makePayment(command, txn_id, txn_date, account, sum, ccy);

    }
}
