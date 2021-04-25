package com.jruchel.mlrest.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component("Properties")
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
    @Value("${regex.email}")
    private String emailRegex;
    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.port}")
    private int mailPort;
    @Value("${mail.ssl}")
    private String mailSsl;
    @Value("${mail.tls}")
    private String mailTsl;
    @Value("${mail.user}")
    private String mailUser;
    @Value("${mail.password}")
    private String mailPassword;
    @Value("${mail.from}")
    private String mailFrom;

    private BuildProperties getBuildProperties() {
        return buildProperties;
    }

    public String version() {
        return getBuildProperties().getVersion();
    }
}
