package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    @Column(name = "gender_probability", nullable = false)
    private Float genderProbability;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "age_group", nullable = false)
    private String ageGroup;

    @Column(name = "country_id", length = 2, nullable = false)
    private String countryId;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "country_probability", nullable = false)
    private Float countryProbability;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

}
