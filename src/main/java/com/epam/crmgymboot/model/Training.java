package com.epam.crmgymboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Trainee trainee;
    @ManyToOne
    private Trainer trainer;
    @Column(nullable = false)
    private String trainingName;
    @Column(nullable = false)
    private LocalDateTime trainingDate;
    @Column(name = "duration", nullable = false)
    private Integer durationInMinutes;

    @PrePersist
    private void setDefaultValues() {
        if (durationInMinutes == null) {
            durationInMinutes = 60;
        }
    }
}
