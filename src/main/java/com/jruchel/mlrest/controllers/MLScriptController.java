package com.jruchel.mlrest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.models.dto.TrainingResult;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.ModelService;
import com.jruchel.mlrest.services.PythonBackendService;
import com.jruchel.mlrest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<String>> getAlgorithms() {
        return new ResponseEntity<>(backendService.getAlgorithms(), HttpStatus.OK);
    }

    @SecuredMapping(path = "/linear-regression/predict", method = RequestMethod.GET)
    public HttpEntity<String> predictLinearRegression(
            @PathParam(value = "modelName") String modelName,
            @RequestBody MultipartFile data,
            @PathParam(value = "separator") String separator,
            @PathParam(value = "predicting") String predicting
    ) throws IOException, URISyntaxException {
        Model model = modelService.findPrincipalModelByName(modelName);
        return new ResponseEntity<>(backendService.predictLinearRegression(model, data, separator, predicting), HttpStatus.OK);
    }

    @SecuredMapping(path = "/linear-regression", method = RequestMethod.GET)
    public ResponseEntity<TrainingResult> linearRegression
            (
                    @RequestBody MultipartFile csv,
                    @PathParam("separator") String separator,
                    @PathParam("predicting") String predicting,
                    @PathParam("save") boolean save,
                    @PathParam("savename") String savename

            ) throws IOException, URISyntaxException {
        User user = userService.loadPrincipalUser();
        UUID userSecret = user.getSecret();
        String response = backendService.linearRegression(csv, separator, predicting, save, savename, userSecret.toString());
        TrainingResult trainingResult = objectMapper.readValue(response, TrainingResult.class);
        if (save) {
            if (trainingResult.getFile() != null) {
                modelService.save(trainingResult.toModel(savename, user));
                if (modelService.findPrincipalModelByName(savename) != null) {
                    trainingResult.setFile("saved");
                } else {
                    trainingResult.setFile("not saved");
                }
            }
        }
        return new ResponseEntity<>(trainingResult, HttpStatus.OK);
    }

    @SecuredMapping(path = "/k-nearest-neighbours", method = RequestMethod.GET)
    public ResponseEntity<String> knn
            (Principal principal,
             @RequestBody MultipartFile csv,
             @PathParam("separator") String separator,
             @PathParam("predicting") String predicting,
             @PathParam("neighbours") int neighbours,
             @PathParam("save") boolean save,
             @PathParam("savename") String savename
            ) throws IOException, URISyntaxException {

        UUID userSecret = userService.loadUserByUsername(principal.getName()).getSecret();
        return new ResponseEntity<>(backendService.knn(csv, separator, predicting, neighbours, save, savename, userSecret.toString()), HttpStatus.OK);

    }
}
