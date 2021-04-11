package com.jruchel.mlrest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.models.dto.LinearRegressionTrainingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseHandlerService {

    private final ObjectMapper objectMapper;
    private final ModelService modelService;

    public LinearRegressionTrainingResult handleLinearRegressionTrainingResponse(String response, boolean save, String saveName, User user) throws JsonProcessingException {
        LinearRegressionTrainingResult linearRegressionTrainingResult = objectMapper.readValue(response, LinearRegressionTrainingResult.class);
        if (save) {
            if (linearRegressionTrainingResult.getFile() != null) {
                modelService.save(linearRegressionTrainingResult.toModel(saveName, user));
                if (modelService.findPrincipalModelByName(saveName) != null) {
                    linearRegressionTrainingResult.setFile(saveName);
                } else {
                    linearRegressionTrainingResult.setFile("not saved");
                }
            }
        }
        return linearRegressionTrainingResult;
    }

}
