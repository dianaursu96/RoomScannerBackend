package com.demo.hotelreservationappbackend.service;


import com.demo.hotelreservationappbackend.dtos.LocationRequestDTO;
import com.demo.hotelreservationappbackend.models.Hotel;
import com.demo.hotelreservationappbackend.repositories.HotelRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    private final HotelRepository hotelRepository;

    @Autowired
    public LocationService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getHotelsInRange(@Valid LocationRequestDTO locationRequestDTO) {
        double userLatitude = Math.toRadians(locationRequestDTO.getUserLatitude());
        double userLongitude = Math.toRadians(locationRequestDTO.getUserLongitude());
        List<Hotel> hotelsAll = hotelRepository.findAll();
        List<Hotel> hotelsInRange = new ArrayList<>();
        for (Hotel hotel : hotelsAll) {
            double hotelLatitude = Math.toRadians(hotel.getLatitude());
            double hotelLongitude = Math.toRadians(hotel.getLongitude());
            double centralAngle = Math.acos(
                    Math.cos(userLatitude) * Math.cos(hotelLatitude) *
                            Math.cos(userLongitude - hotelLongitude) +
                            Math.sin(userLatitude) * Math.sin(hotelLatitude)
            );
            double distance = 6371 * centralAngle;
            if (distance <= locationRequestDTO.getRadius()) {
                hotelsInRange.add(hotel);
            }
        }
        return hotelsInRange;
    }
}
