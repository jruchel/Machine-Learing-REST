package com.jruchel.mlrest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.models.dto.MLModel;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.ModelService;
import com.jruchel.mlrest.services.PythonBackendService;
import com.jruchel.mlrest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/algorithms")
@RequiredArgsConstructor
public class MLScriptController extends Controller {

    private final PythonBackendService backendService;
    private final UserService userService;
    private final ModelService modelService;
    private final ObjectMapper objectMapper;

    @SecuredMapping(path = "", method = RequestMethod.GET)
    public List<String> getAlgorithms() {
        return backendService.getAlgorithms();
    }

    @SecuredMapping(path = "/linear-regression/predict", method = RequestMethod.GET)
    public String predictLinearRegression(
            @PathParam(value = "modelName") String modelName,
            @RequestBody MultipartFile data,
            @PathParam(value = "separator") String separator,
            @PathParam(value = "predicting") String predicting
    ) {
        Model model = modelService.findByUserAndName(userService.loadPrincipalUser(), modelName);
        try {
            return backendService.predictLinearRegression(model, data, separator, predicting);
        } catch (IOException | URISyntaxException e) {
            return e.toString();
        }
    }

    @SecuredMapping(path = "/linear-regression", method = RequestMethod.GET)
    public String linearRegression
            (
                    @RequestBody MultipartFile csv,
                    @PathParam("separator") String separator,
                    @PathParam("predicting") String predicting,
                    @PathParam("save") boolean save,
                    @PathParam("savename") String savename

            ) {
        try {

            boolean saveResult = false;
            User user = userService.loadPrincipalUser();
            UUID userSecret = user.getSecret();
            String response = backendService.linearRegression(csv, separator, predicting, save, savename, userSecret.toString());
            if (save) {
                MLModel mlModel = objectMapper.readValue(response, MLModel.class);
                if (mlModel.getFile() != null) {

                    String encoded = mlModel.getFile();
                    encoded = encoded.replace("b'", "").replace("'", "");
                    Model model = new Model();
                    model.setName(savename);
                    model.setSavedModel(encoded);
                    model.setLastTrainedAccuracy(Double.parseDouble(String.valueOf(mlModel.getAccuracy())));
                    model.setPredictedAttribute(predicting);
                    Model savedModel = modelService.save(model);
                    user.addModel(savedModel);
                    if (modelService.findByUserAndName(user, savename) != null) {
                        saveResult = true;
                    }
                    response = response.replaceAll("\\\"file\\\":.+\\\".+\\\",", String.format("\"saved\":%s,", saveResult));
                }
            }
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @SecuredMapping(path = "/k-nearest-neighbours", method = RequestMethod.GET)
    public String knn
            (Principal principal,
             @RequestBody MultipartFile csv,
             @PathParam("separator") String separator,
             @PathParam("predicting") String predicting,
             @PathParam("neighbours") int neighbours,
             @PathParam("save") boolean save,
             @PathParam("savename") String savename
            ) {
        try {
            UUID userSecret = userService.loadUserByUsername(principal.getName()).getSecret();
            return backendService.knn(csv, separator, predicting, neighbours, save, savename, userSecret.toString());
        } catch (IOException | URISyntaxException e) {
            return e.getMessage();
        }
    }
}
