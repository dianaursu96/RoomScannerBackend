package com.demo.hotelreservationappbackend.service;

import com.demo.hotelreservationappbackend.errors.ReservationCancellationException;
import com.demo.hotelreservationappbackend.errors.ResourceNotFoundException;
import com.demo.hotelreservationappbackend.models.Reservation;
import com.demo.hotelreservationappbackend.models.User;
import com.demo.hotelreservationappbackend.repositories.ReservationRepository;
import com.demo.hotelreservationappbackend.repositories.ReviewRepository;
import com.demo.hotelreservationappbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public AdminService(UserRepository userRepository, ReviewRepository reviewRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public void deleteReservation(Integer reservationId) {
        reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + reservationId));
        reservationRepository.deleteById(reservationId);
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        userRepository.delete(user);
    }


}
