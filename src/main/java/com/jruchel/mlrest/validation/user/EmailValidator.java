package com.jruchel.mlrest.validation.user;

import com.jruchel.mlrest.validation.Validator;

public class EmailValidator extends Validator<EmailConstraint, String> {

    protected boolean Constraint_matchesEmailPattern(String value) {
        if (value == null) return false;
        if (value.isEmpty()) return false;
        return value.matches(properties.getEmailRegex());
    }

}
