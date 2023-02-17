package com.banking.cqrs.core.exceptions;

public class AggregateNotFoundException extends RuntimeException {
    public AggregateNotFoundException(String mesagge){
        super(mesagge);
    }
}
