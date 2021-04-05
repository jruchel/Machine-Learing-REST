package com.jruchel.mlrest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PythonBackendService {

    @Autowired
    private HttpService httpService;

    public String getHelloResponse() throws IOException {
        return httpService.get("http://localhost:5000", "/hello");
    }

}
