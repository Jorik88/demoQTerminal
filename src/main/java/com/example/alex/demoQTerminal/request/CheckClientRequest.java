package com.example.alex.demoQTerminal.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckClientRequest {

    private String command;
    private String txn_id;
    private String account;
    private String sum;
    private String ccy;
}
