package com.jruchel.mlrest.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Properties {
    @Value("${backend-address}")
    private String backendAddress;
    @Value("${test.username}")
    private String testUsername;
    @Value("${test.password}")
    private String testPassword;

}
