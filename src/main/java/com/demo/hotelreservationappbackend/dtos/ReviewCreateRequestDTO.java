package com.demo.hotelreservationappbackend.dtos;


import lombok.Data;

@Data
public class ReviewCreateRequestDTO {
    private String content;
    private Integer cleanliness;
    private Integer staff;
    private Integer facilities;
    private Integer comfort;
    private Integer hotelId;
}
