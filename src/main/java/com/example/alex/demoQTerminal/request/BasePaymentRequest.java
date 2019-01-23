package com.example.alex.demoQTerminal.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasePaymentRequest {

    private String command;
    private String txn_id;
    private String txn_date;
    private String account;
    private String sum;
    private String ccy;

    public BasePaymentRequest(String command, String txn_id, String txn_date, String account, String sum, String ccy) {
        this.command = command;
        this.txn_id = txn_id;
        this.txn_date = txn_date;
        this.account = account;
        this.sum = sum;
        this.ccy = ccy;
    }

    public BasePaymentRequest(String command, String txn_id, String account, String sum, String ccy) {
        this.command = command;
        this.txn_id = txn_id;
        this.account = account;
        this.sum = sum;
        this.ccy = ccy;
    }
}
