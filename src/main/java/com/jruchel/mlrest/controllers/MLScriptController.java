package com.jruchel.mlrest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.ModelService;
import com.jruchel.mlrest.services.PythonBackendService;
import com.jruchel.mlrest.services.UserService;
import lombok.RequiredArgsConstructor;
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

    @SecuredMapping(path = "/linear-regression/predict", method = RequestMethod.GET, role = "user")
    public String predictLinearRegression(Principal principal,
                                          @PathParam(value = "modelName") String modelName,
                                          @RequestBody MultipartFile data,
                                          @PathParam(value = "separator") String separator,
                                          @PathParam(value = "predicting") String predicting
    ) {
        Model model = modelService.findByUserAndName(userService.loadPrincipalUser(principal), modelName);
        try {
            return backendService.predictLinearRegression(model, data, separator, predicting);
        } catch (IOException | URISyntaxException e) {
            return e.toString();
        }
    }

    @SecuredMapping(path = "/linear-regression", method = RequestMethod.GET, role = "user")
    public String linearRegression
            (
                    Principal principal,
                    @RequestBody MultipartFile csv,
                    @PathParam("separator") String separator,
                    @PathParam("predicting") String predicting,
                    @PathParam("save") boolean save,
                    @PathParam("savename") String savename

            ) {
        try {
            boolean saveResult = false;
            User user = userService.loadUserByUsername(principal.getName());
            UUID userSecret = user.getSecret();
            String response = backendService.linearRegression(csv, separator, predicting, save, savename, userSecret.toString());
            if (save) {
                ObjectNode node = objectMapper.readValue(response, ObjectNode.class);
                if (node.has("file")) {
                    String encoded = node.get("file").toString();
                    encoded = omitFirstAndLastCharacter(encoded);
                    byte[] decoded = encoded.getBytes();
                    Model model = new Model();
                    model.setName(savename);
                    model.setSavedModel(decoded);
                    model.setLastTrainedAccuracy(Double.parseDouble(node.get("accuracy").toString()));
                    model.setPredictedAttribute(predicting);
                    user.addModel(model);
                    modelService.save(model);
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

    private String omitFirstAndLastCharacter(String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < string.length() - 1; i++) {
            sb.append(string.charAt(i));
        }
        return sb.toString();
    }

    @SecuredMapping(path = "/k-nearest-neighbours", method = RequestMethod.GET, role = "user")
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
