package com.jruchel.mlrest.services;

import com.jruchel.mlrest.config.Properties;
import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.models.dto.LinearRegressionTrainingResult;
import com.jruchel.mlrest.models.dto.PredictionResults;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
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
    private final ResponseHandlerService responseHandler;

    public List<String> getAlgorithms() {
        List<Object> list = httpService.get(properties.getBackendAddress(), "/algorithms", ArrayList.class).getBody();
        return list.stream().map(o -> o.toString()).collect(Collectors.toList());
    }

    @Async
    public LinearRegressionTrainingResult trainLinearRegression(MultipartFile data, String separator, String predicting, boolean save, String savename, User user) throws IOException, MessagingException {
        LinearRegressionTrainingResult response = linearRegression(data, separator, predicting, save, savename, user.getSecret().toString());
        return (LinearRegressionTrainingResult) responseHandler.handleLinearRegressionTrainingResponse(response, save, savename, user);
    }

    public PredictionResults predict(Model model, MultipartFile data, String separator) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", model.getPredictedAttribute());
        Map<String, Object> formData = new HashMap<>();
        formData.put("modelfile", model.getSavedModel());
        formData.put("data", data);

        return httpService.postMultipartForm(properties.getBackendAddress(), "/algorithms/predict", formData, new HashMap<>(), params, PredictionResults.class).getBody();
    }

    public PredictionResults knn(MultipartFile csvData, String separator, String predicting, int neighbors) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("separator", separator);
        params.put("predicting", predicting);
        params.put("neighbors", String.valueOf(neighbors));
        return algorithm("k-nearest-neighbors", csvData, params, PredictionResults.class);
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
        formData.put("data", csvData);
        return httpService.postMultipartForm(properties.getBackendAddress(), String.format("/algorithms/%s", algorithm), formData, new HashMap<>(), params, c).getBody();
    }

}
