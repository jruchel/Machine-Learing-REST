package com.jruchel.mlrest.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jruchel.mlrest.componets.HttpService;
import com.jruchel.mlrest.config.Properties;
import com.jruchel.mlrest.models.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

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

    public String predictLinearRegression(Model model, MultipartFile data, String separator, String predicting) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", predicting);

        return httpService.get(properties.getBackendAddress(), String.format("/algorithms/%s/predict", "linear-regression"), params, data, model.getSavedModel());
    }

    public String knn(MultipartFile csvData, String separator, String predicting, int neighbours, boolean save, String savename, String userSecret) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", predicting);
        params.put("neighbours", String.valueOf(neighbours));
        params.put("save", String.valueOf(save));
        params.put("savename", savename);
        params.put("usersecret", userSecret);
        return algorithm("k-nearest-neighbours", csvData, params);
    }

    public String linearRegression(MultipartFile csvData, String separator, String predicting, boolean save, String savename, String userSecret) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", predicting);
        params.put("save", String.valueOf(save));
        params.put("savename", savename);
        params.put("usersecret", userSecret);
        return algorithm("linear-regression", csvData, params);
    }

    public String algorithm(String algorithm, MultipartFile csvData, Map<String, String> params) throws IOException, URISyntaxException {
        return httpService.get(properties.getBackendAddress(), String.format("/algorithms/%s", algorithm), params, csvData, null);
    }

}
