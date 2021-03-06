package com.jruchel.mlrest.models.dto;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinearRegressionTrainingResult extends TrainingResult {

    private double accuracy;
    private double intercept;
    private List<Double> coefficients;
    private String file;
    private String predicted;

    public void setFile(String file) {
        this.file = file.replace("b'", "").replace("'", "");
    }

    @Override
    public Model toModel(String name, User user) {
        return toModel(name, user, file, predicted, accuracy);
    }
}
