package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Profile;
import com.example.demo.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ApiResponse<Iterable<Profile>> getProfiles(
            @RequestParam(required = false) String gender,
            @RequestParam(name = "age_group", required = false) String ageGroup,
            @RequestParam(name = "country_id", required = false) String countryId,
            @RequestParam(name = "min_age", required = false) Integer minAge,
            @RequestParam(name = "max_age", required = false) Integer maxAge,
            @RequestParam(name = "min_gender_probability", required = false) Float minGenderProbability,
            @RequestParam(name = "min_country_probability", required = false) Float minCountryProbability,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        return profileService.getProfiles(
                gender, ageGroup, countryId, minAge, maxAge,
                minGenderProbability, minCountryProbability,
                sortBy, order, page, limit);
    }

    @GetMapping("/search")
    public ApiResponse<Iterable<Profile>> searchProfiles(
            @RequestParam String q,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        return profileService.searchProfilesConfig(q, sortBy, order, page, limit);
    }

}
