package com.jruchel.mlrest.services.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PythonBackendService {

    @Autowired
    private HttpService httpService;

    public String getHelloResponse() throws IOException {
        return httpService.get("https://api.punkapi.com", "/v2/beers");
    }

}
