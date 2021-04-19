package com.jruchel.mlrest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    @LocalServerPort
    protected int port;
    @Autowired
    protected TestRestTemplate restTemplate;

    protected String constructUrl(String endpoint) {
        return constructUrl(endpoint, new HashMap<>());
    }

    protected String constructUrl(String endpoint, Map<String, String> pathParams) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://localhost:").append(port).append(endpoint);
        if (pathParams.size() > 0) {
            sb.append("?");
            for (String key : pathParams.keySet()) {
                sb.append(key).append("=").append(pathParams.get(key)).append("&");
            }
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

}
