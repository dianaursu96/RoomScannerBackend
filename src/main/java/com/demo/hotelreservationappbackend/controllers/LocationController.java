package com.demo.hotelreservationappbackend.controllers;

import com.demo.hotelreservationappbackend.dtos.HotelInRangeResponseDTO;
import com.demo.hotelreservationappbackend.dtos.LocationRequestDTO;
import com.demo.hotelreservationappbackend.models.Hotel;
import com.demo.hotelreservationappbackend.service.LocationService;
import com.demo.hotelreservationappbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/location")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping("/hotels")
    public ResponseEntity<List<HotelInRangeResponseDTO>> getHotelsInRange(@RequestParam double userLatitude,
                                                                          @RequestParam double userLongitude,
                                                                          @RequestParam int radius) {
        List<HotelInRangeResponseDTO> hotelsInRange = locationService.getHotelsInRange(userLatitude, userLongitude, radius);
        return ResponseEntity.ok(hotelsInRange);
    }
}
