package com.demo.hotelreservationappbackend.errors;

public class EmailUsedException extends RuntimeException {
    public EmailUsedException(String message) {
        super(message);
    }
}