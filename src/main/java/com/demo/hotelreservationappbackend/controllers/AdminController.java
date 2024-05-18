package com.demo.hotelreservationappbackend.controllers;

import com.demo.hotelreservationappbackend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")

public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @DeleteMapping("/reservation/delete/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Integer reservationId) {
        adminService.deleteReservation(reservationId);
        return ResponseEntity.ok("Reservation canceled successfully!");
    }

    @DeleteMapping("/user/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        adminService.deleteUserByEmail(email);
        return ResponseEntity.ok("User deleted successfully!");
    }

}
