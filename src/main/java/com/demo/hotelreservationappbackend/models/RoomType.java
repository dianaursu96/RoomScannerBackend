package com.demo.hotelreservationappbackend.models;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomType {
    SINGLE("Single"),
    DOUBLE("Double"),
    SUITE("Suite"),
    MATRIMONIAL("Matrimonial");

    private final String roomtype; // ? necessary?
}
