package com.epam.crmgymboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    private TrainingType specialization;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserEntity userEntity;
    @OneToMany(mappedBy = "trainer", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Training> trainings;
}
