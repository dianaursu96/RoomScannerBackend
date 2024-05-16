package com.demo.hotelreservationappbackend.service;

import com.demo.hotelreservationappbackend.dtos.ProfileUpdateRequestDTO;
import com.demo.hotelreservationappbackend.dtos.ReservationCreateRequestDTO;
import com.demo.hotelreservationappbackend.dtos.ReviewCreateRequestDTO;
import com.demo.hotelreservationappbackend.dtos.RoomsAvailabilityRequestDTO;
import com.demo.hotelreservationappbackend.errors.EmailUsedException;
import com.demo.hotelreservationappbackend.errors.InvalidEmailException;
import com.demo.hotelreservationappbackend.errors.ResourceNotFoundException;
import com.demo.hotelreservationappbackend.errors.ReviewPermissionException;
import com.demo.hotelreservationappbackend.models.*;
import com.demo.hotelreservationappbackend.repositories.*;
import com.demo.hotelreservationappbackend.security.UserDetailsImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
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
        if (profileUpdateRequestDTO.getEmail() != null) {
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
    public Hotel retrieveHotelById (Integer hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
    }

    @Transactional
    public List<Room> retrieveRoomsByHotelId (Integer hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }
    @Transactional
    public List<Room> getRoomsAvailabilityForPeriodForHotel(RoomsAvailabilityRequestDTO roomsAvailabilityRequestDTO) {
        List<Room> rooms = retrieveRoomsByHotelId(roomsAvailabilityRequestDTO.getHotelId());

        for (Room room : rooms) {
            boolean isAvailable = true;
            for (Reservation reservation : room.getReservations()) {
                if (reservationsOverlap(roomsAvailabilityRequestDTO.getCheckin(), roomsAvailabilityRequestDTO.getCheckout(), reservation.getCheckin(), reservation.getCheckout())) {
                    isAvailable = false;
                    break;
                }
            }
            room.setIsAvailable(isAvailable);
        }
        return rooms;

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
    public Reservation createReservationForLoggedInUser(ReservationCreateRequestDTO reservationCreateRequestDTO) {
        User user = getLoggedInUser();
        Integer roomId = reservationCreateRequestDTO.getRoomId();

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        List<Reservation> roomReservations = room.getReservations();
        LocalDateTime checkin = reservationCreateRequestDTO.getCheckin();
        LocalDateTime checkout = reservationCreateRequestDTO.getCheckout();
        for (Reservation reservation : roomReservations) {
            if (reservationsOverlap(checkin, checkout, reservation.getCheckin(), reservation.getCheckout())) {
                throw new RuntimeException("Room is not available for the specified period.");
            }
        }
        Reservation newReservation = new Reservation();
        newReservation.setCheckin(checkin);
        newReservation.setCheckout(checkout);
        newReservation.setRoom(room);
        newReservation.setUser(user);
        return reservationRepository.save(newReservation);
    }

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
