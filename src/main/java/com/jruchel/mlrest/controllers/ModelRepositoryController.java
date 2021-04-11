package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.ModelService;
import com.jruchel.mlrest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@CrossOrigin
@RequestMapping("/models")
@RequiredArgsConstructor
public class ModelRepositoryController extends Controller {

    private final ModelService modelService;
    private final UserService userService;

    @SecuredMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<Model> getPrincipalModelFile(@PathParam(value = "name") String name) {
        return new ResponseEntity<>(modelService.findByUserAndName(userService.loadPrincipalUser(), name), HttpStatus.OK);
    }
}
