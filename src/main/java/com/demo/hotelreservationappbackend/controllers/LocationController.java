package com.demo.hotelreservationappbackend.controllers;

import com.demo.hotelreservationappbackend.dtos.LocationRequestDTO;
import com.demo.hotelreservationappbackend.models.Hotel;
import com.demo.hotelreservationappbackend.service.LocationService;
import com.demo.hotelreservationappbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "/location")
//@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @PostMapping("/hotels")
    public ResponseEntity<List<Hotel>> getHotelsInRange(@RequestBody LocationRequestDTO locationRequestDTO) {
        List<Hotel> hotelsInRange = locationService.getHotelsInRange(locationRequestDTO);
        return ResponseEntity.ok(hotelsInRange);
    }



}
