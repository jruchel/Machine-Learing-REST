package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.PythonBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;

@RestController
@CrossOrigin
public class MLScriptController extends Controller {

    @Autowired
    private PythonBackendService backendService;

    @SecuredMapping(path = "/algorithms/linear-regression", method = RequestMethod.GET)
    public String getHello(@RequestBody MultipartFile csv, @PathParam("separator") String separator, @PathParam("predicting") String predicting) {
        try {
            return backendService.linearRegression(csv, separator, predicting);
        } catch (IOException e) {
            return e.getMessage();
        }
    }

}
