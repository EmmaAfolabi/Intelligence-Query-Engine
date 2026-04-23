package com.example.demo.config;

import com.example.demo.dto.ProfileDataWrapper;
import com.example.demo.dto.ProfileDto;
import com.example.demo.model.Profile;
import com.example.demo.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final ProfileRepository profileRepository;

    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            File dataFile = new File("profiles.data");
            if (!dataFile.exists()) {
                log.warn("profiles.data file not found. Skipping data seeding.");
                return;
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ProfileDataWrapper wrapper = objectMapper.readValue(dataFile, ProfileDataWrapper.class);
                if (wrapper != null && wrapper.getProfiles() != null) {
                    List<Profile> toSave = new ArrayList<>();
                    int skipped = 0;

                    for (ProfileDto dto : wrapper.getProfiles()) {
                        if (!profileRepository.existsByName(dto.getName())) {
                            Profile profile = Profile.builder()
                                    .id(UuidCreator.getTimeOrderedEpoch()) // UUID v7
                                    .name(dto.getName())
                                    .gender(dto.getGender())
                                    .genderProbability(dto.getGenderProbability())
                                    .age(dto.getAge())
                                    .ageGroup(dto.getAgeGroup())
                                    .countryId(dto.getCountryId())
                                    .countryName(dto.getCountryName())
                                    .countryProbability(dto.getCountryProbability())
                                    .createdAt(Instant.now())
                                    .build();
                            toSave.add(profile);
                        } else {
                            skipped++;
                        }
                    }

                    if (!toSave.isEmpty()) {
                        profileRepository.saveAll(toSave);
                        log.info("Successfully seeded {} new profiles. Skipped {} existing profiles.", toSave.size(), skipped);
                    } else {
                        log.info("Database is already fully seeded. Skipped {} existing profiles.", skipped);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to seed database: {}", e.getMessage(), e);
            }
        };
    }
}
