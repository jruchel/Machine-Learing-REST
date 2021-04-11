package com.jruchel.mlrest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.models.dto.LinearRegressionTrainingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseHandlerService {

    private final ModelService modelService;

    public LinearRegressionTrainingResult handleLinearRegressionTrainingResponse(LinearRegressionTrainingResult response, boolean save, String saveName, User user) throws JsonProcessingException {
        if (save) {
            if (response.getFile() != null) {
                modelService.save(response.toModel(saveName, user));
                if (modelService.findPrincipalModelByName(saveName) != null) {
                    response.setFile(saveName);
                } else {
                    response.setFile("not saved");
                }
            }
        }
        return response;
    }

}
