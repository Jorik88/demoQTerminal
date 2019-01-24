package com.example.alex.demoQTerminal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class PaymentClientResponse extends CheckClientResponse {

    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    private List<Field> field;

    public PaymentClientResponse(String osmpTxnId, String prvTxn, String sum, String currency, int result, String comment, List<Field> fields) {
        super(osmpTxnId, prvTxn, sum, currency, result, comment);
        this.field = fields;
    }
}
