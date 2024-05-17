package com.demo.hotelreservationappbackend.service;

import com.demo.hotelreservationappbackend.dtos.*;
import com.demo.hotelreservationappbackend.errors.*;
import com.demo.hotelreservationappbackend.models.*;
import com.demo.hotelreservationappbackend.repositories.*;
import com.demo.hotelreservationappbackend.security.UserDetailsImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public UserService(UserRepository userRepository, HotelRepository hotelRepository, RoomRepository roomRepository, ReviewRepository reviewRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        return userDetails.getUser();
    }
    @Transactional
    public User updateLoggedInUser(ProfileUpdateRequestDTO profileUpdateRequestDTO) {

        User loggedInUser = getLoggedInUser();
        if (profileUpdateRequestDTO.getFirstName() != null) {
            loggedInUser.setFirstName(profileUpdateRequestDTO.getFirstName());
        }
        if (profileUpdateRequestDTO.getLastName() != null) {
            loggedInUser.setLastName(profileUpdateRequestDTO.getLastName());
        }
        if (profileUpdateRequestDTO.getEmail() != null && !profileUpdateRequestDTO.getEmail().equals(loggedInUser.getEmail())) {
            if (userRepository.existsByEmail(profileUpdateRequestDTO.getEmail())) {
                throw new EmailUsedException("Email is already in use.");
            }
            if (!isValidEmail(profileUpdateRequestDTO.getEmail())) {
                throw new InvalidEmailException("Invalid email format.");
            }
            loggedInUser.setEmail(profileUpdateRequestDTO.getEmail());
        }
        userRepository.save(loggedInUser);
        return loggedInUser;
    }



    @Transactional
    public Review createReviewForLoggedInUser(ReviewCreateRequestDTO reviewCreateRequestDTO) {
        User user = getLoggedInUser();
        Integer hotelId = reviewCreateRequestDTO.getHotelId();

        if (!hasRightToLeaveReview(user, hotelId)) {
            throw new ReviewPermissionException("Only users who have completed a reservation can leave a review.");
        }
        Hotel hotel = retrieveHotelById(reviewCreateRequestDTO.getHotelId());
        Review review = new Review();
        review.setContent(reviewCreateRequestDTO.getContent());
        review.setCleanliness(reviewCreateRequestDTO.getCleanliness());
        review.setStaff(reviewCreateRequestDTO.getStaff());
        review.setFacilities(reviewCreateRequestDTO.getFacilities());
        review.setComfort(reviewCreateRequestDTO.getComfort());
        review.setHotel(hotel);
        review.setUser(user);
        reviewRepository.save(review);
        return review;
    }

    @Transactional
    public void deleteReviewForLoggedInUser(Integer reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));
        reviewRepository.deleteById(reviewId);
    }


    @Transactional
    public ReservationCreateResponseDTO createReservationForLoggedInUser(Integer roomId, LocalDateTime checkin, LocalDateTime checkout) {
        User user = getLoggedInUser();

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        List<Reservation> roomReservations = room.getReservations();
        for (Reservation reservation : roomReservations) {
            if (reservationsOverlap(checkin, checkout, reservation.getCheckin(), reservation.getCheckout())) {
                throw new RuntimeException("Room is not available for the specified period.");
            }
        }
        // set reservation in database
        Reservation newReservation = new Reservation();
        newReservation.setCheckin(checkin);
        newReservation.setCheckout(checkout);
        newReservation.setRoom(room);
        newReservation.setUser(user);
        reservationRepository.save(newReservation);

        // set responseDTO
        ReservationCreateResponseDTO reservationCreateResponseDTO = new ReservationCreateResponseDTO();
        reservationCreateResponseDTO.setRoomId(roomId);
        reservationCreateResponseDTO.setCheckin(checkin);
        reservationCreateResponseDTO.setCheckout(checkout);
        reservationCreateResponseDTO.setFirstName(user.getFirstName());
        reservationCreateResponseDTO.setLastName(user.getLastName());
        reservationCreateResponseDTO.setEmail(user.getEmail());
        return reservationCreateResponseDTO;
    }

    @Transactional
    public void deleteReservationForLoggedInUser(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + reservationId));

        // verify if he is allowed to cancel (local datetime now + 2h is after reservation.checkin)
        if (LocalDateTime.now().plusHours(2).isAfter(reservation.getCheckin())) {
            throw new ReservationCancellationException("Reservation can be canceled up to 2 hours before check-in");
        }
        reservationRepository.deleteById(reservationId);
    }

    @Transactional
    public Hotel retrieveHotelById (Integer hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
    }

    // helpers

    public boolean hasRightToLeaveReview(User user, Integer hotelId) {
        List<Reservation> reservations = user.getReservations();
        LocalDateTime now = LocalDateTime.now();

        for (Reservation reservation : reservations) {
            if (reservation.getRoom().getHotel().getId().equals(hotelId) &&
                    now.isAfter(reservation.getCheckout())) {
                return true;
            }
        }
        return false;
    }

    private boolean reservationsOverlap(LocalDateTime checkin1, LocalDateTime checkout1, LocalDateTime checkin2, LocalDateTime checkout2) {
        return checkin1.isBefore(checkout2) && checkout1.isAfter(checkin2);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }


}
