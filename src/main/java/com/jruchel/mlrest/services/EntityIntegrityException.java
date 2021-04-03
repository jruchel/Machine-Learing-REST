package com.jruchel.mlrest.services;

public class EntityIntegrityException extends Exception {
    public EntityIntegrityException(String message) {
        super(message);
    }
}
