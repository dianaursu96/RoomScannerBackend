package com.demo.hotelreservationappbackend.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "cleanliness")
    private Integer cleanliness;

    @Column(name = "staff")
    private Integer staff;

    @Column(name = "facilities")
    private Integer facilities;

    @Column(name = "comfort")
    private Integer comfort;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference(value = "user-reviews")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @JsonBackReference(value = "hotel-reviews")
    private Hotel hotel;

}
