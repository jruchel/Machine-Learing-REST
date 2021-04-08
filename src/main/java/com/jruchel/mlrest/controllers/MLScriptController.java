package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.PythonBackendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MLScriptController extends Controller {

    private final PythonBackendService backendService;

    @SecuredMapping(path = "/algorithms", method = RequestMethod.GET)
    public List<String> getAlgorithms() {
        return backendService.getAlgorithms();
    }

    @SecuredMapping(path = "/algorithms/linear-regression", method = RequestMethod.GET)
    public String getHello(@RequestBody MultipartFile csv, @PathParam("separator") String separator, @PathParam("predicting") String predicting) {
        try {
            return backendService.linearRegression(csv, separator, predicting);
        } catch (IOException | URISyntaxException e) {
            return e.getMessage();
        }
    }
}
