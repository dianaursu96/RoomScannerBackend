package com.demo.hotelreservationappbackend.dtos;

import com.demo.hotelreservationappbackend.models.Reservation;
import com.demo.hotelreservationappbackend.models.RoomType;
import lombok.Data;

@Data
public class ReservationDetailsDTO {
        private Reservation reservation;
        private Integer hotelId;
        private String hotelName;
        private RoomType roomType;
        private Integer price;
        private String status;
}
