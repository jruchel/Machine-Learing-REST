package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.models.Role;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.security.Controller;
import com.jruchel.mlrest.security.SecuredMapping;
import com.jruchel.mlrest.security.fillters.JWTUtils;
import com.jruchel.mlrest.services.EntityIntegrityException;
import com.jruchel.mlrest.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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
            return new ResponseEntity<>("Błędne dane logowanie", HttpStatus.OK);
        return new ResponseEntity<>("token:" + JWTUtils.generateToken(user), HttpStatus.OK);
    }

    @SecuredMapping(path = "/register", method = RequestMethod.GET)
    public String register(@RequestBody User user) {
        try {
            user.setRoles(user.getRoles().stream().filter(role -> role.getTitle().equals("user")).collect(Collectors.toSet()));
            securityService.register(user);
            return "registered";
        } catch (EntityIntegrityException e) {
            return e.getMessage();
        }
    }

    @SecuredMapping(path = "/principal", method = RequestMethod.GET)
    public Principal principal(Principal principal) {
        return principal;
    }

    @SecuredMapping(path = "/roles", method = RequestMethod.GET)
    public List<Role> getRoles() {
        return securityService.getAllRoles();
    }

    @SecuredMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getUsers() {
        return securityService.getAllUsers();
    }
}
