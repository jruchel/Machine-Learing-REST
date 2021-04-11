package com.jruchel.mlrest.services;


import com.jruchel.mlrest.exceptions.EntityIntegrityException;
import com.jruchel.mlrest.models.Role;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public User findById(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.orElse(null);
    }

    public void save(User user) throws EntityIntegrityException {
        if (checkEntityIntegrity(user)) {
            userRepository.save(user);
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            users.add(user);
        }
        return users;
    }

    public User loadPrincipalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
    }

    private boolean checkEntityIntegrity(User user) throws EntityIntegrityException {
        try {
            boolean success = true;
            if (user.getRoles().size() < 0) throw new EntityIntegrityException("User cannot exist without roles");
            for (Role r : user.getRoles()) {
                if (r.getUsers().stream().noneMatch(u -> u.getUsername().equals(user.getUsername()))) {
                    success = false;
                    r.addUser(user);
                }
            }
            return success || checkEntityIntegrity(user);

        } catch (Exception ex) {
            throw new EntityIntegrityException(ex.getMessage());
        }
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}