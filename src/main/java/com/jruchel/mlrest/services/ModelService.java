package com.jruchel.mlrest.services;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.repositories.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;

    public void save(Model model) {
        modelRepository.save(model);
    }

    public Model findByUserAndName(User user, String name) {
        return modelRepository.findByName(user.getId(), name);
    }
}
