package com.demo.hotelreservationappbackend.dtos;

import com.demo.hotelreservationappbackend.models.Reservation;
import com.demo.hotelreservationappbackend.models.Review;
import lombok.Data;

import java.util.List;


@Data
public class LoginResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String token;
    private String message;
    private List<Reservation> reservations;
    private List<Review> reviews;

    public LoginResponseDTO(String firstName, String lastName, String email, String token, Long id, String role, String message, List<Reservation> reservations, List<Review> reviews) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.token = token;
        this.id = id;
        this.role = role;
        this.message = message;
        this.reservations = reservations;
        this.reviews = reviews;
    }
}