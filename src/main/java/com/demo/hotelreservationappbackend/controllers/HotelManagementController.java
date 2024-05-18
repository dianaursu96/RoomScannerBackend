package com.demo.hotelreservationappbackend.controllers;
import com.demo.hotelreservationappbackend.dtos.ReservationDetailsDTO;
import com.demo.hotelreservationappbackend.dtos.RoomAvailabilityResponseDTO;
import com.demo.hotelreservationappbackend.models.Hotel;
import com.demo.hotelreservationappbackend.models.Room;
import com.demo.hotelreservationappbackend.service.HotelManagementService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping()
public class HotelManagementController {
    private final HotelManagementService hotelManagementService;

    @Autowired
    public HotelManagementController(HotelManagementService hotelManagementService) {
        this.hotelManagementService = hotelManagementService;
    }

    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<Hotel> retrieveHotelById(@PathVariable Integer hotelId) {
        Hotel hotel = hotelManagementService.retrieveHotelById(hotelId);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/rooms/all/{hotelId}")
    public ResponseEntity<List<Room>> retrieveRoomsByHotelId(@Valid @PathVariable Integer hotelId) {
        List<Room> rooms = hotelManagementService.retrieveRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Room> getRoomById(@Valid @PathVariable Integer roomId) {
        Room room = hotelManagementService.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/rooms/availability")
    public ResponseEntity<List<RoomAvailabilityResponseDTO>> getRoomsAvailabilityForPeriodForHotel(@RequestParam Integer hotelId,
                                                                                                   @RequestParam LocalDateTime checkin,
                                                                                                   @RequestParam LocalDateTime checkout) {
        List<RoomAvailabilityResponseDTO> rooms = hotelManagementService.getRoomsAvailabilityForPeriodForHotel(hotelId, checkin, checkout);
        return ResponseEntity.ok(rooms);
    }




}
