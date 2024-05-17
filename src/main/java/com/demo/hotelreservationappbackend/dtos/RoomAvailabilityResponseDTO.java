package com.demo.hotelreservationappbackend.dtos;

import com.demo.hotelreservationappbackend.models.RoomType;
import lombok.Data;


@Data
public class RoomAvailabilityResponseDTO {
    private Integer id;
    private Integer number;
    private RoomType type;
    private Integer price;
    private String imageURL;
    private Boolean isAvailable;
}
