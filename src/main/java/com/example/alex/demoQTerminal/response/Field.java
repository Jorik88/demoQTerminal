package com.example.alex.demoQTerminal.response;

import com.example.alex.demoQTerminal.utils.DateAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "field")
@XmlAccessorType(XmlAccessType.FIELD)
public class Field {

    @XmlAttribute(name = "name")
    private String name;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlValue
    private Date value;

}
