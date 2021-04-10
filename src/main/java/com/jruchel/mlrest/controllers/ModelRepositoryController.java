package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.ModelService;
import com.jruchel.mlrest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/models")
@RequiredArgsConstructor
public class ModelRepositoryController extends Controller {

    private final ModelService modelService;
    private final UserService userService;

    @SecuredMapping(path = "", method = RequestMethod.GET, role = "user")
    public Model getModelFile(Principal principal, @PathParam(value = "name") String name) {
        return modelService.findByUserAndName(userService.loadPrincipalUser(principal), name);
    }
}
