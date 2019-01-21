package com.example.alex.demoQTerminal.service;

import com.example.alex.demoQTerminal.model.UserAccountInfo;
import com.example.alex.demoQTerminal.request.CheckClientRequest;
import com.example.alex.demoQTerminal.response.CheckClientResponse;
import com.example.alex.demoQTerminal.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QiwiPaymentTerminalService {

    private static final String PAYMENT_ID_PREFIX = "QT_";


    public CheckClientResponse checkClient(String command, String tnx_id, String account, String sum, String ccy) {
        CheckClientRequest clientRequest = new CheckClientRequest(command, tnx_id, account, sum, ccy);

        try {

            UserAccountInfo userAccountInfo = checkInternal(clientRequest);

            CheckClientResponse clientResponse = new CheckClientResponse();
            clientResponse.setOsmpTxnId(clientRequest.getTxn_id());
            clientResponse.setPrvTxn(generatePaymentId(userAccountInfo.getId()));
            clientResponse.setCurrency(clientRequest.getCcy());
            clientResponse.setSum(clientRequest.getSum());
            clientResponse.setComment("Ok");
            clientResponse.setResult(ResponseStatus.OK);
            return clientResponse;
        }catch (Exception e) {
            log.warn("Error check user account, request={}", clientRequest);
            return fillBaseResponse(clientRequest.getTxn_id(),ResponseStatus.CHECK_ACCOUNT_ERROR);
        }
    }

    private CheckClientResponse fillBaseResponse(String txn_id, ResponseStatus checkAccountError) {
        CheckClientResponse clientResponse = new CheckClientResponse();
        clientResponse.setOsmpTxnId(txn_id);
        clientResponse.setResult(checkAccountError);
        return clientResponse;
    }

    private UserAccountInfo checkInternal(CheckClientRequest clientRequest) {
//        Add checks for authorization service(find user by email)

        return  new UserAccountInfo();
    }

    private String generatePaymentId(String userId) {
        return PAYMENT_ID_PREFIX + userId + "_" + System.currentTimeMillis();
    }

    public void makePayment(String command, String txn_id, String txn_date, String account, String sum, String ccy) {

    }
}
