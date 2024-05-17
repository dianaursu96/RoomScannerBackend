package com.demo.hotelreservationappbackend.dtos;

import com.demo.hotelreservationappbackend.models.Review;
import com.demo.hotelreservationappbackend.models.Room;
import lombok.Data;

import java.util.List;

@Data
public class HotelInRangeResponseDTO {
    private Integer id;
    private String name;
    private String imageURL;
    private List<Room> rooms;
    private List<Review> reviews;
    private double averageRate;
    private int distanceFromCenter;

}
