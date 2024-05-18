package com.demo.hotelreservationappbackend.dtos;


import lombok.Data;
import lombok.Getter;


@Getter
@Data
public class LocationRequestDTO {
    private double userLatitude;
    private double userLongitude;
    private int radius;
}


