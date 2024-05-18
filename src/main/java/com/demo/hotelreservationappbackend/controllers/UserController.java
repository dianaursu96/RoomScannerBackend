package com.demo.hotelreservationappbackend.controllers;


import com.demo.hotelreservationappbackend.dtos.*;
import com.demo.hotelreservationappbackend.models.*;
import com.demo.hotelreservationappbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
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

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReviewForLoggedInUser(@PathVariable Integer reviewId) {
        userService.deleteReviewForLoggedInUser(reviewId);
        return ResponseEntity.ok("Review deleted successfully!");
    }

    @PostMapping("/review/create")
    public ResponseEntity<Review> createReviewForLoggedInUser(@Valid @RequestBody ReviewCreateRequestDTO reviewCreateRequestDTO) {
            Review review = userService.createReviewForLoggedInUser(reviewCreateRequestDTO);
            return ResponseEntity.ok(review);
    }

    @PostMapping("/reservation/create")
    public ResponseEntity<ReservationCreateResponseDTO> createReservationForLoggedInUser(@RequestParam Integer roomId,
                                                                                         @RequestParam LocalDateTime checkin,
                                                                                         @RequestParam LocalDateTime checkout) {
        ReservationCreateResponseDTO reservationCreateResponseDTO  = userService.createReservationForLoggedInUser(roomId, checkin, checkout);
        return ResponseEntity.ok(reservationCreateResponseDTO);
    }
    @DeleteMapping("/reservation/delete/{reservationId}")
    public ResponseEntity<?> deleteReservationForLoggedInUser(@PathVariable Integer reservationId) {
        userService.deleteReservationForLoggedInUser(reservationId);
        return ResponseEntity.ok("Reservation canceled successfully!");
    }
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDetailsDTO>> getReservationsDetailsForLoggedInUser() {
        List<ReservationDetailsDTO> reservationsDetails = userService.getReservationsDetailsForLoggedInUser();
        return ResponseEntity.ok(reservationsDetails);
    }


}
