package com.demo.hotelreservationappbackend.repositories;

import com.demo.hotelreservationappbackend.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {


}
