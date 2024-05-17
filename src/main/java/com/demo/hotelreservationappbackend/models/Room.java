package com.demo.hotelreservationappbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number")
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50)
    private RoomType type;

    @Column(name = "price")
    private Integer price;

    @Column(name = "image_url")
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @JsonBackReference(value = "rooms")
    private Hotel hotel;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonManagedReference(value = "room-reservations")
    private List<Reservation> reservations;



}
