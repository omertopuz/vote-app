package org.example.domain.exception;

public class EntityNotFoundException extends VoteDomainException{
    public EntityNotFoundException(String message) {
        super(message);
    }
}
