package com.jruchel.mlrest.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinearRegressionTrainingInput {
    private String separator;
    private String predicting;
    private boolean save;
    private String saveName;
    private String userSecret;
}
