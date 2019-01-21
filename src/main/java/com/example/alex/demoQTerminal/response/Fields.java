package com.example.alex.demoQTerminal.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "fields")
public class Fields {

    private List<Field> fields;
}
