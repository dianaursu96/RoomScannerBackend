package com.demo.hotelreservationappbackend.dtos;

import lombok.Data;

@Data
public class ProfileUpdateRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
}
