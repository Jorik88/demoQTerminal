package com.example.alex.demoQTerminal.response;

import com.migesok.jaxb.adapter.javatime.LocalDateTimeXmlAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "field")
@XmlAccessorType(XmlAccessType.FIELD)
public class Field {

    @XmlAttribute(name = "name")
    private String name;

    @XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
    @XmlValue
    private LocalDateTime value;

}
