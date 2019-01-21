package com.example.alex.demoQTerminal.response;

import lombok.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@Data
@XmlRootElement(name = "response")
public class CheckClientResponse {

    @XmlElement(name = "osmp_txn_id")
    private String osmpTxnId;

    @XmlElement(name = "prv_txn")
    private String prvTxn;

    @XmlElement(name = "sum")
    private String sum;

    @XmlElement(name = "ccy")
    private String currency;

    @XmlElement(name = "result")
    private ResponseStatus result;

    @XmlElement(name = "comment")
    private String comment;

}
