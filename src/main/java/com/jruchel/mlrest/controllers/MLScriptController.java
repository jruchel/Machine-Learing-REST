package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.models.dto.LinearRegressionTrainingResult;
import com.jruchel.mlrest.models.dto.PredictionResults;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.ModelService;
import com.jruchel.mlrest.services.PythonBackendService;
import com.jruchel.mlrest.services.ResponseHandlerService;
import com.jruchel.mlrest.services.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/algorithms")
@RequiredArgsConstructor
public class MLScriptController extends Controller {

    private final PythonBackendService backendService;
    private final UserService userService;
    private final ModelService modelService;
    private final ResponseHandlerService responseHandler;

    @ApiOperation(value = "See what algorithms are available",
            notes = "Returns a list of available machine learning algorithms",
            authorizations = {
                    @Authorization(value = "user")
            },
            response = List.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = List.class)
    })
    @SecuredMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAlgorithms() {
        return new ResponseEntity<>(backendService.getAlgorithms(), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Use an already trained model to make new predictions",
            notes = "Finds a principal model by name and makes predictions based on the given csv data.",
            authorizations = {
                    @Authorization(value = "user")
            },
            response = List.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = List.class),
            @ApiResponse(code = 409, message = "Conflict", response = String.class)
    })
    @SecuredMapping(path = "/predict", method = RequestMethod.POST, role = "user")
    public ResponseEntity<PredictionResults> predict(
            @ApiParam(required = true, name = "modelName", value = "Name of a trained model", type = "String", example = "linear-regression-stock-values")
            @PathParam(value = "modelName") String modelName,
            @ApiParam(required = true, name = "data", value = "Data to base the predictions off")
            @RequestBody MultipartFile data,
            @ApiParam(required = true, name = "separator", value = "The separator sign that the csv data uses", example = ",")
            @PathParam(value = "separator") String separator
    ) throws IOException {
        Model model = modelService.findPrincipalModelByName(modelName);
        return new ResponseEntity<>(backendService.predict(model, data, separator), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Train a linear regression model using a csv file",
            notes = "Uses the given csv data to train a linear regression model for the given attribute.",
            authorizations = {
                    @Authorization(value = "user")
            },
            response = String.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 409, message = "Conflict", response = String.class)
    })
    @SecuredMapping(path = "/linear-regression", method = RequestMethod.POST, role = "user")
    public ResponseEntity<String> linearRegression
            (
                    @ApiParam(required = true, name = "data", value = "Data to train on.")
                    @RequestBody MultipartFile data,
                    @ApiParam(required = true, name = "separator", value = "Separator of the csv data file.")
                    @PathParam("separator") String separator,
                    @ApiParam(required = true, name = "predicting", value = "Attribute to train to predict.")
                    @PathParam("predicting") String predicting,
                    @ApiParam(required = true, name = "save", value = "Whether the result model should be saved.")
                    @PathParam("save") boolean save,
                    @ApiParam(required = true, name = "savename", value = "Name to save the model as.")
                    @PathParam("savename") String savename

            ) throws IOException {
        User user = userService.loadPrincipalUser();
        trainLinearRegression(data, separator, predicting, save, savename, user);
        return new ResponseEntity<>("Training has started, once it is finished, the results will be sent to your email and the model will be saved on your account.", HttpStatus.valueOf(202));
    }

    @Async
    public LinearRegressionTrainingResult trainLinearRegression(MultipartFile data, String separator, String predicting, boolean save, String savename, User user) throws IOException {
        LinearRegressionTrainingResult response = backendService.linearRegression(data, separator, predicting, save, savename, user.getSecret().toString());
        return (LinearRegressionTrainingResult) responseHandler.handleLinearRegressionTrainingResponse(response, save, savename, user);
    }

    @ApiOperation(
            value = "Classify data using k nearest neighbors algorithm",
            notes = "Classifies the data in the given csv file using k nearest neighbors algorithm.",
            authorizations = {
                    @Authorization(value = "user")
            },
            response = PredictionResults.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = PredictionResults.class),
            @ApiResponse(code = 409, message = "Conflict", response = String.class)
    })
    @SecuredMapping(path = "/k-nearest-neighbors", method = RequestMethod.POST, role = "user")
    public ResponseEntity<PredictionResults> knn
            (
                    @ApiParam(required = true, name = "data", value = "Data to train on.")
                    @RequestBody MultipartFile data,
                    @ApiParam(required = true, name = "separator", value = "Separator of the csv data file.")
                    @PathParam("separator") String separator,
                    @ApiParam(required = true, name = "predicting", value = "Attribute to train to classify.")
                    @PathParam("predicting") String predicting,
                    @ApiParam(required = true, name = "neighbors", value = "Number of neighbors to use.")
                    @PathParam("neighbors") int neighbors
            ) throws IOException {
        return new ResponseEntity<>(backendService.knn(data, separator, predicting, neighbors), HttpStatus.OK);

    }
}
