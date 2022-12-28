package com.topjava.restaurant_voting.exeption;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super(message + " not found");
    }
}
