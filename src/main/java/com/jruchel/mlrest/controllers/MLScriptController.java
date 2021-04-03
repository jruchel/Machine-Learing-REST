package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.PythonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.io.IOException;


@RestController
@CrossOrigin
public class MLScriptController extends Controller {

    @Autowired
    private PythonService pythonService;

    @SecuredMapping(path = "/script", method = RequestMethod.GET)
    public String runScript(@PathParam("script") String script) {
        try {
            return pythonService.runScript(script);
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
