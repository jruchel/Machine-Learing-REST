package com.jruchel.mlrest.models.dto;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinearRegressionTrainingResult {

    private double accuracy;
    private double intercept;
    private List<Double> coefficients;
    private String file;
    private String predicted;


    public void setFile(String file) {
        this.file = file.replace("b'", "").replace("'", "");
    }

    public Model toModel(String name, User user) {
        Model model = new Model();
        model.setName(name);
        model.setSavedModel(file);
        model.setPredictedAttribute(predicted);
        model.setLastTrainedAccuracy(accuracy);
        user.addModel(model);
        return model;
    }
}
