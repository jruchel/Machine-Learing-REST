package com.jruchel.mlrest.services;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.repositories.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;
    private final UserService userService;


    public Model save(Model model) {
        return modelRepository.save(model);
    }

    public Model findPrincipalModelByName(String name) {
        return modelRepository.findByOwnerAndName(userService.loadPrincipalUser(), name);
    }
}
