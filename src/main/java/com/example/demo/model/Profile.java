package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("gender_probability")
    @Column(name = "gender_probability", nullable = false)
    private Float genderProbability;

    @Column(nullable = false)
    private Integer age;

    @JsonProperty("age_group")
    @Column(name = "age_group", nullable = false)
    private String ageGroup;

    @JsonProperty("country_id")
    @Column(name = "country_id", length = 2, nullable = false)
    private String countryId;

    @JsonProperty("country_name")
    @Column(name = "country_name", nullable = false)
    private String countryName;

    @JsonProperty("country_probability")
    @Column(name = "country_probability", nullable = false)
    private Float countryProbability;

    @JsonProperty("created_at")
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

}
