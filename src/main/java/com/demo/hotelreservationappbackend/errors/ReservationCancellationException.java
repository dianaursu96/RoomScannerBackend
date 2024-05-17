package com.demo.hotelreservationappbackend.errors;

public class ReservationCancellationException extends RuntimeException {
    public ReservationCancellationException(String message) {
        super(message);
    }
}
