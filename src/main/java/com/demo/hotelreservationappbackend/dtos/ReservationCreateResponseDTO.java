package com.demo.hotelreservationappbackend.dtos;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ReservationCreateResponseDTO {

    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private Integer roomId;
    private String firstName;
    private String lastName;
    private String email;

}
