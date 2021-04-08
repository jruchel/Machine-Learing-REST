package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.ModelService;
import com.jruchel.mlrest.services.UserService;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ModelRepositoryController extends Controller {

    private final ModelService modelService;
    private final UserService userService;

    @SecuredMapping(path = "/save", role = "user", method = RequestMethod.POST)
    public String save(@RequestBody Pair<User, MultipartFile> userFilePair, @PathParam(value = "name") String name) {
        User user = userFilePair.getKey();
        user = userService.loadUserByUsername(user.getUsername());
        if (user == null) return "User does not exist";

        MultipartFile file = userFilePair.getValue();
        Model model = new Model();
        model.setName(name);
        model.setOwner(user);
        try {
            model.setSavedModel(file.getBytes());
        } catch (IOException e) {
            return String.format("Unable to save file %s", file.getName());
        }
        modelService.save(model);
        return String.format("%s saved", file.getName());
    }
}
