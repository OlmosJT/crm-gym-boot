package com.epam.crmgymboot.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer failedAttempt = 0;
    @Column(nullable = true)
    private LocalDateTime lockTime;
}
