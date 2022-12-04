package org.example.domain.exception;

public class UserAlreadyVotedException extends VoteDomainException{
    public UserAlreadyVotedException() {
        super("Duplication not allowed. User already voted.");
    }
}
