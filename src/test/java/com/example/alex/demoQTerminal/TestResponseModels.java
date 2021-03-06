package com.example.alex.demoQTerminal;

import com.example.alex.demoQTerminal.response.*;
import org.junit.Test;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

public class TestResponseModels {

    @Test
    public void testPaymentClientResponse() {
        PaymentClientResponse clientResponse =
                new PaymentClientResponse("code", "own_code", "32.3", "USD",
                        ResponseStatus.OK.getStatusCode(), "OK",
                        Arrays.asList(new Field("prv-date", new Date()), new Field("prv-date", new Date())));
        writeToFile(clientResponse, PaymentClientResponse.class, "file3.xml");

    }

    @Test
    public void testFillBaseResponse() {
        CheckClientResponse clientResponse = new CheckClientResponse();
        clientResponse.setOsmpTxnId("tnx_code");
        clientResponse.setResult(ResponseStatus.CHECK_ACCOUNT_ERROR.getStatusCode());
       writeToFile(clientResponse, CheckClientResponse.class, "file2.xml");
    }


    @Test
    public void testCheckClientResponse() {
        CheckClientResponse clientResponse =
                new CheckClientResponse(
                        "code",
                        "code2",
                        "45.5",
                        "USD",
                        ResponseStatus.OK.getStatusCode(),
                        "Ok");
       writeToFile(clientResponse, CheckClientResponse.class, "file.xml");
    }

    private <T> void writeToFile(T someRequest, Class clazz, String filePath) {
        try {

            File file = new File(filePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(someRequest, file);
            jaxbMarshaller.marshal(someRequest, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDate() {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        System.out.println(df.format(new Date()));

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(1523261382698L), ZoneId.systemDefault()).withNano(0);
        System.out.println(localDateTime);
    }

    @Test
    public void testGeneratePaymentId() {
        System.out.println("QT_123_" + System.currentTimeMillis());
    }

    @Test
    public void testConvertAmount() {
        System.out.println(new BigDecimal("23.3"));
        System.out.println(String.valueOf(new BigDecimal(3423.354).doubleValue()));
    }

}
