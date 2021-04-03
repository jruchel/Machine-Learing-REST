package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.PythonBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin
public class MLScriptController extends Controller {

    @Autowired
    private PythonBackendService backendService;

    @SecuredMapping(path = "/", method = RequestMethod.GET)
    public String getHello() {
        try {
            return backendService.getHelloResponse();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

}
