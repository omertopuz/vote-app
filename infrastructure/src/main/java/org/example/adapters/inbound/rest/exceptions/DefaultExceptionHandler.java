package org.example.adapters.inbound.rest.exceptions;

import org.example.domain.exception.EntityNotFoundException;
import org.example.domain.exception.VoteDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = {VoteDomainException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDetails apiException(VoteDomainException ex) {
        return ErrorDetails.builder()
                .exceptionType(ErrorDetails.ExceptionTypeEnum.API_EXCEPTION)
                .message(ex.getMessage())
                .time(new Date())
                .build();
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorDetails notFoundException(EntityNotFoundException ex) {
        return ErrorDetails.builder()
                .exceptionType(ErrorDetails.ExceptionTypeEnum.NOT_FOUND_EXCEPTION)
                .message(ex.getMessage())
                .time(new Date())
                .build();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDetails unKnownException(Exception ex) {
        return ErrorDetails.builder()
                .exceptionType(ErrorDetails.ExceptionTypeEnum.EXCEPTION)
                .message("Unexpected Error occurred: "+ex.getMessage())
                .time(new Date())
                .build();
    }

}
