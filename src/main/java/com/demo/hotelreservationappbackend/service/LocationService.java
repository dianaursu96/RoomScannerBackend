package com.demo.hotelreservationappbackend.service;


import com.demo.hotelreservationappbackend.dtos.HotelInRangeResponseDTO;
import com.demo.hotelreservationappbackend.dtos.LocationRequestDTO;
import com.demo.hotelreservationappbackend.models.Hotel;
import com.demo.hotelreservationappbackend.models.Review;
import com.demo.hotelreservationappbackend.repositories.HotelRepository;
import com.demo.hotelreservationappbackend.repositories.ReviewRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    private final HotelRepository hotelRepository;
    @Autowired
    public LocationService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<HotelInRangeResponseDTO> getHotelsInRange(double userLatitude, double userLongitude, int radius) {
        double userLatitudeRad = Math.toRadians(userLatitude);
        double userLongitudeRad = Math.toRadians(userLongitude);
        List<Hotel> hotelsAll = hotelRepository.findAll();
        List<HotelInRangeResponseDTO> hotelsInRangeResponseDTO = new ArrayList<>();

        for (Hotel hotel : hotelsAll) {
            HotelInRangeResponseDTO hotelInRangeResponseDTO = new HotelInRangeResponseDTO();
            hotelInRangeResponseDTO.setId(hotel.getId());
            hotelInRangeResponseDTO.setName(hotel.getName());
            hotelInRangeResponseDTO.setImageURL(hotel.getImageURL());
            hotelInRangeResponseDTO.setReviews(hotel.getReviews());
            hotelInRangeResponseDTO.setRooms(hotel.getRooms());
            hotelInRangeResponseDTO.setAverageRate(averageRatingForHotel(hotel));

            // use the GCD formula to get distance from the center
            double distance = getDistance(hotel, userLatitudeRad, userLongitudeRad);
            if (distance <= radius) {
                hotelInRangeResponseDTO.setDistanceFromCenter((int) distance);
                hotelsInRangeResponseDTO.add(hotelInRangeResponseDTO);
            }
        }
        return hotelsInRangeResponseDTO;
    }

    // helpers

    private static double getDistance(Hotel hotel, double userLatitudeRad, double userLongitudeRad) {
        double hotelLatitude = Math.toRadians(hotel.getLatitude());
        double hotelLongitude = Math.toRadians(hotel.getLongitude());
        double centralAngle = Math.acos(
                Math.cos(userLatitudeRad) * Math.cos(hotelLatitude) *
                        Math.cos(userLongitudeRad - hotelLongitude) +
                        Math.sin(userLatitudeRad) * Math.sin(hotelLatitude)
        );
        return 6371 * centralAngle;
    }

    public double averageRatingForHotel(Hotel hotel) {
        List<Review> reviews = hotel.getReviews();
        double averageRating;
        double totalSum = 0;
        for (Review review : reviews) {
            double averageReview =  (double) (review.getCleanliness() + review.getComfort() + review.getStaff() + review.getFacilities()) / 4;
            totalSum += averageReview;
        }
        averageRating = totalSum / reviews.size();
        return averageRating;
    }
}
