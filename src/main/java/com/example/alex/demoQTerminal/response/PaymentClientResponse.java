package com.example.alex.demoQTerminal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class PaymentClientResponse extends CheckClientResponse {

    @XmlElement(name = "fields")
    private Fields fields;

    public PaymentClientResponse(String osmpTxnId, String prvTxn, String sum, String currency, int result, String comment, Fields fields) {
        super(osmpTxnId, prvTxn, sum, currency, result, comment);
        this.fields = fields;
    }
}
