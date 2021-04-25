package com.jruchel.mlrest.validation.user;

import com.jruchel.mlrest.validation.Validator;

public class EmailValidator extends Validator<EmailConstraint, String> {

    protected String Constraint_notNullOrEmpty(String value) {
        if (value == null) return "Email cannot be null";
        if (value.isEmpty()) return "Email cannot be empty";
        return "true";
    }

    protected String Constraint_matchesEmailPattern(String value) {
        if (value == null) return "null is not a valid email";
        if (!value.matches(properties.getEmailRegex())) return String.format("\"%s\" is not a valid email", value);
        return "true";
    }

}
