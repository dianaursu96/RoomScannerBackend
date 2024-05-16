package com.demo.hotelreservationappbackend.errors;

public class ResourceNotFoundException  extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

}
