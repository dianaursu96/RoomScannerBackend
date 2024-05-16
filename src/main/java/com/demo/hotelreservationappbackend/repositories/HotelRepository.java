package com.demo.hotelreservationappbackend.repositories;

import com.demo.hotelreservationappbackend.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
}
