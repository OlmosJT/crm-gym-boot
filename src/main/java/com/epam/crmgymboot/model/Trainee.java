package com.epam.crmgymboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * --CascadeType.ALL--
     * If I create a new Trainee(new User()) and persist it to database
     * new User() entity also persists to database.
     * I do not need to save user first and trainee.
     *
     * If I update Trainee instance and user via referencing trainee.user().setAddress()
     * and merge it, both entities are updated.
     *
     * If I remove Trainee entity from database, User entity is also removed.
     * I do not have to create a userService here!
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserEntity userEntity;

    @OneToMany(mappedBy = "trainee", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Training> trainings;

}
