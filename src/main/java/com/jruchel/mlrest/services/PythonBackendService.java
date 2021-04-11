package com.jruchel.mlrest.services;

import com.jruchel.mlrest.config.Properties;
import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.dto.LinearRegressionTrainingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PythonBackendService {

    private final HttpService httpService;
    private final Properties properties;

    public List<String> getAlgorithms() {
        List<Object> list = httpService.get(properties.getBackendAddress(), "/algorithms", ArrayList.class).getBody();
        return list.stream().map(o -> o.toString()).collect(Collectors.toList());
    }

    public String predictLinearRegression(Model model, MultipartFile data, String separator, String predicting) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", predicting);
        Map<String, Object> formData = new HashMap<>();
        formData.put("modelfile", model.getSavedModel());
        formData.put("data", data);

        return httpService.postMultipartForm(properties.getBackendAddress(), "/algorithms/linear-regression/predict", formData, new HashMap<>(), params, String.class).getBody();
    }

    public String knn(MultipartFile csvData, String separator, String predicting, int neighbours, boolean save, String savename, String userSecret) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", predicting);
        params.put("neighbours", String.valueOf(neighbours));
        params.put("save", String.valueOf(save));
        params.put("savename", savename);
        params.put("usersecret", userSecret);
        return algorithm("k-nearest-neighbours", csvData, params, String.class);
    }

    public LinearRegressionTrainingResult linearRegression(MultipartFile csvData, String separator, String predicting, boolean save, String savename, String userSecret) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", predicting);
        params.put("save", String.valueOf(save));
        params.put("savename", savename);
        params.put("usersecret", userSecret);
        return algorithm("linear-regression", csvData, params, LinearRegressionTrainingResult.class);
    }

    public <T> T algorithm(String algorithm, MultipartFile csvData, Map<String, String> params, Class<T> c) throws IOException {
        Map<String, Object> formData = new HashMap<>();
        formData.put("trainingData", csvData);
        return httpService.postMultipartForm(properties.getBackendAddress(), String.format("/algorithms/%s", algorithm), formData, new HashMap<>(), params, c).getBody();
    }

}
