package com.jruchel.mlrest.services;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.repositories.ModelRepository;
import org.springframework.stereotype.Service;


@Service
public class ModelService {

    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public Model save(Model model) {
        return modelRepository.save(model);
    }

    public Model findByUserAndName(User user, String name) {
        return modelRepository.findByName(user.getId(), name);
    }
}
