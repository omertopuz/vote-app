package org.example.domain.exception;

public class SecurityItemInvalidException extends VoteDomainException{
    public SecurityItemInvalidException() {
        super("Security item invalid");
    }
}
