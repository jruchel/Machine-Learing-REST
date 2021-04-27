package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.exceptions.ValidationException;
import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.services.ModelService;
import com.jruchel.mlrest.services.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelService modelService;

    @ApiOperation(
            value = "See the models of the currently authenticated user.",
            notes = "Returns a Set of trained and ready to use machine learning models of the current user",
            authorizations = {
                    @Authorization("user")
            }
    )
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Boolean.class)})
    @SecuredMapping(path = "/models", method = RequestMethod.GET, role = "user")
    public ResponseEntity<Set<Model>> getUserModels() {
        return new ResponseEntity<>(userService.getUserModels(userService.loadPrincipalUser()), HttpStatus.OK);
    }

    @ApiOperation(
            value = "See the currently authenticated user's details.",
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

    @ApiOperation(
            value = "Delete one of currently authenticated user's models.",
            notes = "Returns the deleted model.",
            authorizations = {
                    @Authorization("user")
            }
    )
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Boolean.class)})
    @SecuredMapping(path = "/models", method = RequestMethod.DELETE, role = "user")
    public ResponseEntity<Model> deleteUserModel(
            @ApiParam(name = "modelName", value = "Name of the model to delete.",
                    required = true, example = "students-linear-regression")
            @PathParam("modelName") String modelName) throws ValidationException {
        return new ResponseEntity<>(modelService.deletePrincipalModelByName(modelName), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Find one of currently authenticated user's models by name.",
            notes = "Returns the model.",
            authorizations = {
                    @Authorization("user")
            }
    )
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Boolean.class)})
    @SecuredMapping(path = "/models/model", method = RequestMethod.DELETE, role = "user")
    public ResponseEntity<Model> getUserModelByName(@ApiParam(name = "modelName", value = "Name of the model to delete.",
            required = true, example = "students-linear-regression")
                                                    @PathParam("modelName") String modelName) throws ValidationException {
        return new ResponseEntity<>(modelService.deletePrincipalModelByName(modelName), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Change the name of one of currently authenticated user's models.",
            notes = "Returns the model with the new name.",
            authorizations = {
                    @Authorization("user")
            }
    )
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Boolean.class)})
    @SecuredMapping(path = "/models", method = RequestMethod.PUT, role = "user")
    public ResponseEntity<Model> changeUserModelName(
            @ApiParam(name = "modelName", value = "Name of the model to change.",
                    required = true, example = "students-linear-regressin")
            @PathParam("modelName") String modelName,
            @ApiParam(name = "newModel", value = "Name to change to.",
                    required = true, example = "students-linear-regression")
            @PathParam("newName") String newName) throws ValidationException {
        return new ResponseEntity<>(modelService.changePrincipalModelName(modelName, newName), HttpStatus.OK);
    }


}
