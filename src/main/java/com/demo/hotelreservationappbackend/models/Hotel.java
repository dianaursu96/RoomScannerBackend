package com.demo.hotelreservationappbackend.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hotels")
//@JsonIgnoreProperties("rooms")
public class Hotel  {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double  longitude;

    @Column(name = "image_url")
    private String imageURL;

    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference(value = "rooms")
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    @JsonManagedReference(value = "hotel-reviews")
    @ToString.Exclude
    private List<Review> reviews;


}
