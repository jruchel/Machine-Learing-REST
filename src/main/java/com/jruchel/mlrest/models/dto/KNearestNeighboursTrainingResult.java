package com.jruchel.mlrest.models.dto;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KNearestNeighboursTrainingResult extends TrainingResult {
    private double accuracy;
    private int neighbours;
    private String file;
    private String predicted;

    @Override
    public void setFile(String file) {
        this.file = file.replace("b'", "").replace("'", "");
    }

    @Override
    public String getFile() {
        return file;
    }

    @Override
    public Model toModel(String name, User user) {
        return toModel(name, user, file, predicted, accuracy);
    }
}
