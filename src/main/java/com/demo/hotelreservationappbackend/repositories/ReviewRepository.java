package com.demo.hotelreservationappbackend.repositories;

import com.demo.hotelreservationappbackend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
