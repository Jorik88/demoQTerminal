package com.example.alex.demoQTerminal.response;

import lombok.Getter;

public enum ResponseStatus {
    OK(0),
    CHECK_ACCOUNT_ERROR(5),
    PAYMENT_ERROR(7);

    @Getter
    private int statusCode;

    ResponseStatus(int statusCode) {
        this.statusCode = statusCode;
    }
}
