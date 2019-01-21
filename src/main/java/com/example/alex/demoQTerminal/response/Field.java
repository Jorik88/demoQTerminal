package com.example.alex.demoQTerminal.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "field")
public class Field {

    @XmlAttribute(name = "name")
    private String name;

}
