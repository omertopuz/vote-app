package org.example.domain.exception;

public class VoteDomainException extends RuntimeException{

    public VoteDomainException(){

    }

    public VoteDomainException(String message){
        super(message);
    }
}
