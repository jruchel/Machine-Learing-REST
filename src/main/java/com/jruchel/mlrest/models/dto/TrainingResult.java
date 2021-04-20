package com.jruchel.mlrest.models.dto;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;

public abstract class TrainingResult {

    public abstract void setFile(String file);

    public abstract String getFile();

    public abstract Model toModel(String name, User user);

    protected Model toModel(String name, User user, String file, String predicted, double accuracy) {
        Model model = new Model();
        model.setName(name);
        model.setSavedModel(file);
        model.setPredictedAttribute(predicted);
        model.setLastTrainedAccuracy(accuracy);
        model.setAlgorithm(getAlgorithm());
        user.addModel(model);
        return model;
    }

    protected String getAlgorithm() {
        String className = getClass().getSimpleName();
        return className.replaceAll("TrainingResult", "");
    }
}
