package com.epam.crmgymboot.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String firstname;
    @Column(nullable = false, length = 50)
    private String lastname;
    @Column(unique = true, nullable = false)
    private String username;
    private String address;
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;
    @Column(nullable = false, length = 15)
    private String password;
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private Boolean isActive;
}
