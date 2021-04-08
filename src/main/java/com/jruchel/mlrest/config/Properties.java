package com.jruchel.mlrest.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Properties {

    @Value("${backend-address}")
    private String backendAddress;


}
