package com.demo.hotelreservationappbackend.dtos;

import lombok.Data;
@Data
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String password;
    public RegisterRequestDTO() {
        this.role = "USER";
    }

    public RegisterRequestDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = "USER";
    }
}
