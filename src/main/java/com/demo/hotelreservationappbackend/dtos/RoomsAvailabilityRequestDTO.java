package com.demo.hotelreservationappbackend.dtos;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomsAvailabilityRequestDTO {
    private Integer hotelId;
    private LocalDateTime checkin;
    private LocalDateTime checkout;
}
