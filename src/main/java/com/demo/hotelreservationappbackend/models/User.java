package com.demo.hotelreservationappbackend.models;


import com.demo.hotelreservationappbackend.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotEmpty
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @JsonIgnore
    @ToString.Exclude
    @Column(name = "password", nullable = false)
    private String password;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @ToString.Exclude
    private Role role;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-reservations")
    @ToString.Exclude
    @JsonIgnore
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-reviews")
    @ToString.Exclude
    @JsonIgnore
    private List<Review> reviews;


}
