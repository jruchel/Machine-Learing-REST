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
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
                    encoded = encoded.replaceAll("\"", "");
                    byte[] decoded = Base64Utils.decode(encoded.getBytes(StandardCharsets.UTF_8));
                    Model model = new Model();
                    model.setName(savename);
                    model.setSavedModel(decoded);
                    user.addModel(model);
                    modelService.save(model);
                    if (modelService.findByUserAndName(user, savename) != null) {
                        saveResult = true;
                    }
                    response = response.replaceAll(String.format("\"%s\":\"%s\"", "file", encoded), String.format("saved:%s", saveResult));
                }
            }
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
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
