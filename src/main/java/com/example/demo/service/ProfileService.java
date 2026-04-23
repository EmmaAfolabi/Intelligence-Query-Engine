package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Profile;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.ProfileSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ApiResponse<Iterable<Profile>> getProfiles(
            String gender, String ageGroup, String countryId,
            Integer minAge, Integer maxAge, Float minGenderProbability,
            Float minCountryProbability,
            String sortBy, String order, int page, int limit) {

        Specification<Profile> spec = ProfileSpecification.byFilters(
                gender, ageGroup, countryId, minAge, maxAge, minGenderProbability, minCountryProbability);

        Pageable pageable = buildPageable(sortBy, order, page, limit);
        Page<Profile> result = profileRepository.findAll(spec, pageable);

        return ApiResponse.successPage(result.getContent(), page, limit, result.getTotalElements());
    }

    public ApiResponse<Iterable<Profile>> searchProfilesConfig(String q, String sortBy, String order, int page, int limit) {
        if (q == null || q.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid query parameters");
        }

        String lowerQ = q.toLowerCase().trim();

        String gender = null;
        String ageGroup = null;
        String countryId = null;
        String countryName = null;
        Integer minAge = null;
        Integer maxAge = null;

        boolean understood = false;

        if (lowerQ.contains("young")) {
            minAge = 16;
            maxAge = 24;
            understood = true;
        }
        boolean hasFemale = lowerQ.contains("female") || lowerQ.contains("females") || lowerQ.contains("women") || lowerQ.contains("girls");
        boolean hasMale = lowerQ.contains("male") || lowerQ.contains("males") || lowerQ.contains("men") || lowerQ.contains("boys");

        if (hasFemale && hasMale) {
            gender = null;
            understood = true;
        } else if (hasFemale) {
            gender = "female";
            understood = true;
        } else if (hasMale) {
            gender = "male";
            understood = true;
        }

        if (lowerQ.contains("adult") || lowerQ.contains("adults")) {
            ageGroup = "adult";
            understood = true;
        } else if (lowerQ.contains("teenager") || lowerQ.contains("teenagers")) {
            ageGroup = "teenager";
            understood = true;
        } else if (lowerQ.contains("child") || lowerQ.contains("children")) {
            ageGroup = "child";
            understood = true;
        } else if (lowerQ.contains("senior") || lowerQ.contains("seniors")) {
            ageGroup = "senior";
            understood = true;
        }
        
        if (lowerQ.contains("people")) {
            understood = true;
        }

        if (lowerQ.contains("above ")) {
            try {
                int idx = lowerQ.indexOf("above ");
                String after = lowerQ.substring(idx + 6).trim();
                String[] words = after.split("\\s+");
                minAge = Integer.parseInt(words[0]);
                understood = true;
            } catch (Exception ignored) {}
        }
        if (lowerQ.contains("below ")) {
            try {
                int idx = lowerQ.indexOf("below ");
                String after = lowerQ.substring(idx + 6).trim();
                String[] words = after.split("\\s+");
                maxAge = Integer.parseInt(words[0]);
                understood = true;
            } catch (Exception ignored) {}
        }

        if (lowerQ.contains("from ")) {
            try {
                int idx = lowerQ.indexOf("from ");
                countryName = lowerQ.substring(idx + 5).trim();
                // attempt to find exact matching ISO if possible
                countryId = mapCountryNameToId(countryName);
                understood = true;
            } catch (Exception ignored) {}
        }

        if (!understood) {
            throw new IllegalArgumentException("Unable to interpret query");
        }

        // Apply filters
        Specification<Profile> spec = ProfileSpecification.byFilters(
                gender, ageGroup, countryId, minAge, maxAge, null, null);

        // if country Name is used but exact code not found, let's map it dynamic
        if (countryId == null && countryName != null) {
            final String fCountryName = countryName;
            Specification<Profile> nameSpec = (root, query, cb) -> cb.equal(cb.lower(root.get("countryName")), fCountryName);
            spec = spec.and(nameSpec);
        }

        Pageable pageable = buildPageable(sortBy, order, page, limit);
        Page<Profile> result = profileRepository.findAll(spec, pageable);

        return ApiResponse.successPage(result.getContent(), page, limit, result.getTotalElements());
    }

    private Pageable buildPageable(String sortBy, String order, int page, int limit) {
        if (page < 1) page = 1;
        if (limit < 1) limit = 10;
        if (limit > 50) limit = 50;

        Sort.Direction direction = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(order)) {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equalsIgnoreCase("age") || sortBy.equalsIgnoreCase("created_at") || sortBy.equalsIgnoreCase("gender_probability")) {
                String actualSort = sortBy;
                if (sortBy.equalsIgnoreCase("created_at")) actualSort = "createdAt";
                if (sortBy.equalsIgnoreCase("gender_probability")) actualSort = "genderProbability";
                sort = Sort.by(direction, actualSort);
            }
        }

        return PageRequest.of(page - 1, limit, sort);
    }

    private String mapCountryNameToId(String name) {
        Map<String, String> countryMap = new HashMap<>();
        countryMap.put("nigeria", "NG");
        countryMap.put("angola", "AO");
        countryMap.put("kenya", "KE");
        countryMap.put("tanzania", "TZ");
        countryMap.put("uganda", "UG");
        countryMap.put("sudan", "SD");
        countryMap.put("united states", "US");
        countryMap.put("madagascar", "MG");
        countryMap.put("united kingdom", "GB");
        countryMap.put("india", "IN");
        countryMap.put("cameroon", "CM");
        countryMap.put("cape verde", "CV");
        countryMap.put("republic of the congo", "CG");
        countryMap.put("dr congo", "CD");
        countryMap.put("mozambique", "MZ");
        countryMap.put("south africa", "ZA");
        countryMap.put("mali", "ML");
        countryMap.put("france", "FR");
        countryMap.put("zambia", "ZM");
        countryMap.put("eritrea", "ER");
        countryMap.put("gabon", "GA");
        countryMap.put("rwanda", "RW");
        countryMap.put("senegal", "SN");
        countryMap.put("namibia", "NA");
        countryMap.put("benin", "BJ");
        countryMap.put("egypt", "EG");
        countryMap.put("ghana", "GH");
        countryMap.put("western sahara", "EH");
        countryMap.put("australia", "AU");
        countryMap.put("morocco", "MA");
        countryMap.put("brazil", "BR");
        countryMap.put("tunisia", "TN");
        countryMap.put("somalia", "SO");
        countryMap.put("zimbabwe", "ZW");
        countryMap.put("gambia", "GM");
        countryMap.put("côte d'ivoire", "CI");
        countryMap.put("ethiopia", "ET");
        countryMap.put("malawi", "MW");
        return countryMap.getOrDefault(name.toLowerCase(), null);
    }
}
