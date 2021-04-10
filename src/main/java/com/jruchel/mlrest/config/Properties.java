package com.jruchel.mlrest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ml")
public class Properties {

    private String backendAddress;
    private String dupa;

}
