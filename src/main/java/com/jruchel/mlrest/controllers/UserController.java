package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
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

    @ApiOperation(
            value = "See the models of the currently authenticated user.",
            notes = "Returns a Set of trained and ready to use machine learning models of the current user",
            authorizations = {
                    @Authorization("user")
            }
    )
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Boolean.class)})
    @SecuredMapping(path = "models", method = RequestMethod.GET, role = "user")
    public ResponseEntity<Set<Model>> getUserModels() {
        return new ResponseEntity<>(userService.getUserModels(userService.loadPrincipalUser()), HttpStatus.OK);
    }

    @ApiOperation(
            value = "See the currenlty authenticated user's details.",
            notes = "Username, encoded password, email, models and so on",
            authorizations = {
                    @Authorization("user")
            }
    )
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Boolean.class)})
    @SecuredMapping(path = "", method = RequestMethod.GET, role = "user")
    public ResponseEntity<User> getUserDetails() {
        return new ResponseEntity<>(userService.loadPrincipalUser(), HttpStatus.OK);
    }

}
