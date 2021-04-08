package com.jruchel.mlrest.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jruchel.mlrest.config.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PythonBackendService {

    private final HttpService httpService;
    private final Properties properties;
    private final ObjectMapper objectMapper;

    public List<String> getAlgorithms() {
        try {
            String json = httpService.get(properties.getBackendAddress(), "/algorithms");
            return objectMapper.readValue(json, new TypeReference<List<String>>() {
            });
        } catch (IOException | URISyntaxException e) {
            return new ArrayList<>();
        }
    }

    public String linearRegression(MultipartFile csvData, String separator, String predicting) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", predicting);
        return httpService.get(properties.getBackendAddress(), "/algorithms/linear-regression", params, csvData);
    }

}
