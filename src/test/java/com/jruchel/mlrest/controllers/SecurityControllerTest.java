package com.jruchel.mlrest.controllers;

import com.jruchel.mlrest.exceptions.EntityIntegrityException;
import com.jruchel.mlrest.models.Role;
import com.jruchel.mlrest.models.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SecurityControllerTest extends ControllerTest {

    @Autowired
    private SecurityController securityController;


    private User mockUser() {
        User user = new User();
        user.setPassword("admin");
        user.setUsername("admin");
        Role userRole = new Role();
        userRole.setTitle("user");
        user.grantRole(userRole);

        return user;
    }

    @Test
    @Order(1)
    public void registerUserWithEmptyRoles() throws EntityIntegrityException {
        User mockUser = mockUser();
        mockUser.setRoles(new HashSet<>());
        ResponseEntity<Boolean> response = securityController.register(mockUser);

        assertEquals(409, response.getStatusCode().value());
        assertEquals(false, response.getBody());
    }

    @Test
    @Order(2)
    public void authenticateNonExistentUser() {
        ResponseEntity<String> response = securityController.authenticate(mockUser());
        assertEquals(409, response.getStatusCode().value());
    }

    @Test
    @Order(3)
    public void registerMockUser() throws EntityIntegrityException {
        ResponseEntity<Boolean> response = securityController.register(mockUser());

        assertEquals(201, response.getStatusCode().value());
        assertEquals(true, response.getBody());

    }

    @Test
    @Order(4)
    public void authenticateMockUser() {
        ResponseEntity<String> response = securityController.authenticate(mockUser());
        assertEquals(response.getStatusCode().value(), 200);

        assertTrue(Pattern.matches("token:[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+", response.getBody()));

    }


}