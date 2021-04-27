package com.jruchel.mlrest.services;

import com.jruchel.mlrest.exceptions.ValidationException;
import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
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

    public Model delete(Model model) throws ValidationException {
        Model deletedModel = modelRepository.findById(model.getId()).orElse(null);
        if (deletedModel == null)
            throw new ValidationException(String.format("Model '%s' does not exist for user '%s'", model.getName(), model.getOwner()));
        modelRepository.deleteById(model.getId());
        return deletedModel;
    }

    public Model changePrincipalModelName(String name, String newName) throws ValidationException {
        User principal = userService.loadPrincipalUser();
        Model model = findPrincipalModelByName(name);
        if (model == null)
            throw new ValidationException(String.format("Model '%s' does not exists for user '%s'", newName, principal.getUsername()));
        if (findPrincipalModelByName(newName) != null)
            throw new ValidationException(String.format("Model '%s' already exists for user '%s'", newName, model.getOwner().getUsername()));
        model.setName(newName);
        return save(model);
    }

    public Model deletePrincipalModelByName(String name) throws ValidationException {
        Model model = findPrincipalModelByName(name);
        return delete(model);
    }

    public Model findPrincipalModelByName(String name) {
        return modelRepository.findByOwnerAndName(userService.loadPrincipalUser(), name);
    }
}
