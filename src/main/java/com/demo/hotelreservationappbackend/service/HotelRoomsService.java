package com.demo.hotelreservationappbackend.service;

import com.demo.hotelreservationappbackend.models.Hotel;
import com.demo.hotelreservationappbackend.models.Room;
import com.demo.hotelreservationappbackend.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelRoomsService {

    private final HotelRepository hotelRepository;


    @Autowired
    public HotelRoomsService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public void saveHotels(List<Hotel> hotels) {
       for (Hotel hotel : hotels) {
            for (Room room : hotel.getRooms()) {
                room.setHotel(hotel);
            }
        }
        hotelRepository.saveAll(hotels);
    }
}
