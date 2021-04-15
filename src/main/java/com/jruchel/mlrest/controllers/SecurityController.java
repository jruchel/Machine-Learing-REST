package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.security.fillters.JWTUtils;
import com.jruchel.mlrest.exceptions.EntityIntegrityException;
import com.jruchel.mlrest.security.SecurityService;
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

    @SecuredMapping(path = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<String> authorize(@RequestBody User user) {
        if (!securityService.authenticate(user.getUsername(), user.getPassword()))
            return new ResponseEntity<>("Incorrect username or password.", HttpStatus.valueOf(409));
        return new ResponseEntity<>("token:" + JWTUtils.generateToken(user), HttpStatus.valueOf(200));
    }

    @SecuredMapping(path = "/register", method = RequestMethod.GET)
    public ResponseEntity<Boolean> register(@RequestBody User user) throws EntityIntegrityException {
        user.setRoles(user.getRoles().stream().filter(role -> role.getTitle().equals("user") || role.getTitle().equals("ROLE_USER")).collect(Collectors.toSet()));
        boolean registered = securityService.register(user);
        return new ResponseEntity<>(registered, HttpStatus.valueOf(registered ? 201 : 409));
    }
}
