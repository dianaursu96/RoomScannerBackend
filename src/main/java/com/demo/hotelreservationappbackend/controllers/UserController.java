package com.demo.hotelreservationappbackend.controllers;


import com.demo.hotelreservationappbackend.dtos.ProfileUpdateRequestDTO;
import com.demo.hotelreservationappbackend.dtos.ReservationCreateRequestDTO;
import com.demo.hotelreservationappbackend.dtos.ReviewCreateRequestDTO;
import com.demo.hotelreservationappbackend.dtos.RoomsAvailabilityRequestDTO;
import com.demo.hotelreservationappbackend.models.*;
import com.demo.hotelreservationappbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
//@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping("/update")
    public ResponseEntity<User> updateLoggedInUser(@Valid @RequestBody ProfileUpdateRequestDTO profileUpdateRequest) {
        User updatedUser = userService.updateLoggedInUser(profileUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<Hotel> retrieveHotelById( @PathVariable Integer hotelId) {
        Hotel hotel = userService.retrieveHotelById(hotelId);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/rooms/all/{hotelId}")
    public ResponseEntity<List<Room>> retrieveRoomsByHotelId(@Valid @PathVariable Integer hotelId) {
        List<Room> rooms = userService.retrieveRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/rooms/availability")
    public ResponseEntity<List<Room>> getRoomsAvailabilityForPeriodForHotel(@RequestBody RoomsAvailabilityRequestDTO roomsAvailabilityRequestDTO) {
        List<Room> rooms = userService.getRoomsAvailabilityForPeriodForHotel(roomsAvailabilityRequestDTO);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/review/create")
    public ResponseEntity<Review> createReviewForLoggedInUser(@Valid @RequestBody ReviewCreateRequestDTO reviewCreateRequestDTO) {
            Review review = userService.createReviewForLoggedInUser(reviewCreateRequestDTO);
            return ResponseEntity.ok(review);
    }

    @PostMapping("/reservation/create")
    public ResponseEntity<Reservation> createReservationForLoggedInUser(@Valid @RequestBody ReservationCreateRequestDTO reservationCreateRequestDTO) {
        Reservation reservation = userService.createReservationForLoggedInUser(reservationCreateRequestDTO);
        return ResponseEntity.ok(reservation);
    }



}
