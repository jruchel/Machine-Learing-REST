package com.jruchel.mlrest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PythonBackendService {

    @Autowired
    private HttpService httpService;

    public String linearRegression(MultipartFile csvData, String separator, String predicting) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", predicting);
        return httpService.get("http://localhost:5000", "/algorithms/linear-regression", params, csvData);
    }

}
