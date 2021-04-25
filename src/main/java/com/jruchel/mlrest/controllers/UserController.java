package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @SecuredMapping(path = "models", method = RequestMethod.GET, role = "user")
    public ResponseEntity<Set<Model>> getUserModels() {
        return new ResponseEntity<>(userService.getUserModels(userService.loadPrincipalUser()), HttpStatus.OK);
    }

    @SecuredMapping(path = "", method = RequestMethod.GET, role = "user")
    public ResponseEntity<User> getUserDetails() {
        return new ResponseEntity<>(userService.loadPrincipalUser(), HttpStatus.OK);
    }

}
