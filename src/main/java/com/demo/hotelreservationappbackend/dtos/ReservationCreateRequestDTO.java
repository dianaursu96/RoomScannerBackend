package com.demo.hotelreservationappbackend.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationCreateRequestDTO {


    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private Integer roomId;
}
