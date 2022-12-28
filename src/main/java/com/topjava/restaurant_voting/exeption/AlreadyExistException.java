package com.topjava.restaurant_voting.exeption;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message + " already exist");
    }
}
