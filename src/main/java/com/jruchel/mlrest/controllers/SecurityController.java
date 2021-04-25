package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.exceptions.EntityIntegrityException;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.security.SecurityService;
import com.jruchel.mlrest.security.fillters.JWTUtils;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityController extends Controller {
    private final SecurityService securityService;

    @ApiOperation(
            value = "Authenticate and receive a JWT token.",
            notes = "Performs authentication on the given user and if successful, returns a valid JWT token.",
            authorizations = {
                    @Authorization(value = "user")
            },
            response = String.class
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Success", response = String.class),
                    @ApiResponse(code = 409, message = "Conflict", response = String.class)
            }
    )
    @SecuredMapping(path = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<String> authenticate(@ApiParam(name = "user", value = "User to authenticate", required = true) @RequestBody User user) {
        if (!securityService.authenticate(user.getUsername(), user.getPassword()))
            return new ResponseEntity<>("Incorrect username or password.", HttpStatus.valueOf(409));
        return new ResponseEntity<>("token:" + JWTUtils.generateToken(user), HttpStatus.valueOf(200));
    }

    @ApiOperation(value = "Register a new user.", notes = "If possible performs registration for the given user.", response = Boolean.class)
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Success", response = Boolean.class),
                    @ApiResponse(code = 409, message = "Conflict", response = Boolean.class)
            }
    )
    @SecuredMapping(path = "/register", method = RequestMethod.GET)
    public ResponseEntity<Boolean> register(@ApiParam(name = "user", value = "User to register, only the username and password are taken into account.", required = true, example = "{\"username\":\"user\", \"password\":\"password\"}") @RequestBody User user) throws EntityIntegrityException {
        user.setRoles(user.getRoles().stream().filter(role -> role.getTitle().equals("user") || role.getTitle().equals("ROLE_USER")).collect(Collectors.toSet()));
        boolean registered = securityService.register(user);
        return new ResponseEntity<>(registered, HttpStatus.valueOf(registered ? 201 : 409));
    }
}
