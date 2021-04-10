package com.jruchel.mlrest.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MLModel {

    private double accuracy;
    private double intercept;
    private List<Double> coefficients;
    private String file;
}
