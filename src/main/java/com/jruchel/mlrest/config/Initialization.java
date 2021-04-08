package com.jruchel.mlrest.config;

import com.jruchel.mlrest.models.Role;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.services.EntityIntegrityException;
import com.jruchel.mlrest.services.RoleService;
import com.jruchel.mlrest.services.SecurityService;
import com.jruchel.mlrest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Initialization {

    private final UserService userService;
    private final RoleService roleService;
    private final SecurityService securityService;

    private List<Role> createRoles(String... titles) {
        List<Role> roles = new ArrayList<>();
        for (String title : titles) {
            roles.add(createRole(title));
        }
        return roles;
    }

    private Role createRole(String title) {
        Role role = roleService.getRoleByTitle(title);
        if (role == null) {
            role = new Role();
            role.setTitle(title);
            roleService.save(role);
        }
        return role;
    }

    private User createUser(String username, String password, List<Role> roles) throws EntityIntegrityException {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        for (Role r : roles) {
            user.grantRole(r);
        }
        securityService.register(user);
        return user;
    }

    @PostConstruct
    private void initialize() {
        User user = null;
        try {
            user = createUser("kuba", "admin", createRoles("user"));
        } catch (EntityIntegrityException e) {
            System.out.printf("User: %s could not be created.%n", user == null ? "null" : user.toString());
        }
    }
}
