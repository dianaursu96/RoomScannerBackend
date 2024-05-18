package com.demo.hotelreservationappbackend.service;

import com.demo.hotelreservationappbackend.dtos.ReservationDetailsDTO;
import com.demo.hotelreservationappbackend.dtos.RoomAvailabilityResponseDTO;
import com.demo.hotelreservationappbackend.errors.ResourceNotFoundException;
import com.demo.hotelreservationappbackend.models.Hotel;
import com.demo.hotelreservationappbackend.models.Reservation;
import com.demo.hotelreservationappbackend.models.Review;
import com.demo.hotelreservationappbackend.models.Room;
import com.demo.hotelreservationappbackend.repositories.HotelRepository;
import com.demo.hotelreservationappbackend.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelManagementService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;


    @Autowired
    public HotelManagementService(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Hotel retrieveHotelById (Integer hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
    }

    @Transactional
    public Room getRoomById(Integer roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + roomId));
    }

    @Transactional
    public List<Room> retrieveRoomsByHotelId (Integer hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    @Transactional
    public List<RoomAvailabilityResponseDTO> getRoomsAvailabilityForPeriodForHotel(Integer hotelId, LocalDateTime checkin, LocalDateTime checkout) {
        List<Room> rooms = retrieveRoomsByHotelId(hotelId);
        List<RoomAvailabilityResponseDTO> roomsAvailability = new ArrayList<>();

        for (Room room : rooms) {
            RoomAvailabilityResponseDTO roomAvailabilityResponseDTO = new RoomAvailabilityResponseDTO();
            roomAvailabilityResponseDTO.setId(room.getId());
            roomAvailabilityResponseDTO.setType(room.getType());
            roomAvailabilityResponseDTO.setNumber(room.getNumber());
            roomAvailabilityResponseDTO.setPrice(room.getPrice());
            roomAvailabilityResponseDTO.setImageURL(room.getImageURL());
            boolean isAvailable = true;

            for (Reservation reservation : room.getReservations()) {
                if (reservationsOverlap(checkin, checkout, reservation.getCheckin(), reservation.getCheckout())) {
                    isAvailable = false;
                    break;
                }
            }
            roomAvailabilityResponseDTO.setIsAvailable(isAvailable);
            roomsAvailability.add(roomAvailabilityResponseDTO);
        }
        return roomsAvailability;
    }


    @Transactional
    public void saveHotels(List<Hotel> hotels) {
       for (Hotel hotel : hotels) {
            for (Room room : hotel.getRooms()) {
                room.setHotel(hotel);
            }
        }
        hotelRepository.saveAll(hotels);
    }


    //helper
    private boolean reservationsOverlap(LocalDateTime checkin1, LocalDateTime checkout1, LocalDateTime checkin2, LocalDateTime checkout2) {
        return checkin1.isBefore(checkout2) && checkout1.isAfter(checkin2);
    }



}
