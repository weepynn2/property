package com.g3.property.entity;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "Email")
    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email is mandatory.")
    private String email;

    @Column(nullable = false, name = "First_Name")
    @NotBlank(message = "First name is mandatory.")
    private String firstName;

    @Column(nullable = false, name = "Last_Name")
    @NotBlank(message = "Last name is mandatory.")
    private String lastName;

    @Column(nullable = false, name = "Phone_Number")
    private String phoneNumber;

    @ManyToMany
    @JoinTable(
        name = "user_favourites",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "listing_id")
    )
    private List<Listing> favouriteListings;

    public void addFavourite(Listing listing) {
        this.favouriteListings.add(listing);
    }

}
