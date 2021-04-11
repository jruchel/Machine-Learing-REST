package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.models.dto.LinearRegressionTrainingResult;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.ModelService;
import com.jruchel.mlrest.services.PythonBackendService;
import com.jruchel.mlrest.services.ResponseHandlerService;
import com.jruchel.mlrest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
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
    private final ResponseHandlerService responseHandler;

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
    ) throws IOException {
        Model model = modelService.findPrincipalModelByName(modelName);
        return new ResponseEntity<>(backendService.predictLinearRegression(model, data, separator, predicting), HttpStatus.OK);
    }

    @SecuredMapping(path = "/linear-regression", method = RequestMethod.GET)
    public ResponseEntity<LinearRegressionTrainingResult> linearRegression
            (
                    @RequestBody MultipartFile csv,
                    @PathParam("separator") String separator,
                    @PathParam("predicting") String predicting,
                    @PathParam("save") boolean save,
                    @PathParam("savename") String savename

            ) throws IOException {
        User user = userService.loadPrincipalUser();
        UUID userSecret = user.getSecret();
        LinearRegressionTrainingResult response = backendService.linearRegression(csv, separator, predicting, save, savename, userSecret.toString());

        return new ResponseEntity<>(responseHandler.handleLinearRegressionTrainingResponse(response, save, savename, user), HttpStatus.OK);
    }

    @SecuredMapping(path = "/k-nearest-neighbours", method = RequestMethod.GET)
    public ResponseEntity<String> knn
            (
                    @RequestBody MultipartFile csv,
                    @PathParam("separator") String separator,
                    @PathParam("predicting") String predicting,
                    @PathParam("neighbours") int neighbours,
                    @PathParam("save") boolean save,
                    @PathParam("savename") String savename
            ) throws IOException {

        UUID userSecret = userService.loadPrincipalUser().getSecret();
        return new ResponseEntity<>(backendService.knn(csv, separator, predicting, neighbours, save, savename, userSecret.toString()), HttpStatus.OK);

    }
}
