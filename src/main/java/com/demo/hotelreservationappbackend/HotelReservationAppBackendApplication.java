package com.demo.hotelreservationappbackend;

import com.demo.hotelreservationappbackend.models.Hotel;
import com.demo.hotelreservationappbackend.service.HotelManagementService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class HotelReservationAppBackendApplication {



    @Bean
    CommandLineRunner runner(HotelManagementService hotelManagementService) {
        return args -> {
            // read json and write hotels and rooms to db
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Hotel>> typeReference = new TypeReference<>(){};
            InputStream inputStream = TypeReference.class.getResourceAsStream("/json/hotels.json");
            try {
                List<Hotel> hotels = mapper.readValue(inputStream, typeReference);
                hotelManagementService.saveHotels(hotels);
                System.out.println("Hotels saved");
            } catch (IOException e) {
                System.out.println("unable to save  " + e.getMessage());
            }
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(HotelReservationAppBackendApplication.class, args);
    }

}
