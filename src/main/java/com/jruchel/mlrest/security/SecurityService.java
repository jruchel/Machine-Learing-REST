package com.jruchel.mlrest.security;

import com.jruchel.mlrest.models.Role;
import com.jruchel.mlrest.exceptions.EntityIntegrityException;
import com.jruchel.mlrest.services.RoleService;
import com.jruchel.mlrest.services.UserService;
import lombok.RequiredArgsConstructor;
import com.jruchel.mlrest.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public boolean authenticate(String username, String password) {
        UserDetails fromDB = userService.loadUserByUsername(username);
        if (fromDB == null) return false;
        if (!fromDB.getUsername().equals(username)) return false;
        return passwordEncoder.matches(password, fromDB.getPassword());
    }

    private void assignCorrectRoles(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role userRole = new Role();
            userRole.setTitle("user");
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
        }
        Set<Role> correctRoles = new HashSet<>();
        for (Role r : user.getRoles()) {
            correctRoles.add(roleService.getRoleByTitle(r.getTitle()));
        }
        user.setRoles(correctRoles);
    }

    private boolean validateRegistration(User user) {
        return (userService.loadUserByUsername(user.getUsername()) == null);

    }

    public boolean register(User user) throws EntityIntegrityException {
        if (!validateRegistration(user)) return false;
        assignCorrectRoles(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return true;
    }

    public List<Role> getAllRoles() {
        return roleService.findAll();
    }

    public List<User> getAllUsers() {
        return userService.findAll();
    }
}
