package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.PythonService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.io.IOException;


@RestController
@CrossOrigin
public class MLScriptController extends Controller {

    private final PythonService pythonService;

    public MLScriptController(PythonService pythonService) {
        this.pythonService = pythonService;
    }

    @SecuredMapping(path = "/script", method = RequestMethod.GET)
    public String runScript(@PathParam("script") String script) {
        return getScriptResult(script);
    }

    @SecuredMapping(path = "/ml", method = RequestMethod.GET)
    public String runML(@PathParam("message") String message) {
        return getScriptResult("ml", message);
    }

    private String getScriptResult(String script, String... args) {
        try {
            return pythonService.runScript(script, args);
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
