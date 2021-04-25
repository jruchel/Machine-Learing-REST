package com.jruchel.mlrest.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Properties {
    @Autowired
    private BuildProperties buildProperties;

    @Value("${backend-address}")
    private String backendAddress;
    @Value("${test.username}")
    private String testUsername;
    @Value("${test.password}")
    private String testPassword;

    private BuildProperties getBuildProperties() {
        return buildProperties;
    }

    public String version() {
        return buildProperties.getVersion();
    }
}
