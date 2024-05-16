package com.demo.hotelreservationappbackend.errors;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException (String message) {
        super(message);
    }
}
