package com.example.alex.demoQTerminal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccountInfo {
    private String id;
    private String email;
}
