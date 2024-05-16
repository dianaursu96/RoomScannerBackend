package com.demo.hotelreservationappbackend.errors;

public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException(String message) {
        super(message);
    }

}