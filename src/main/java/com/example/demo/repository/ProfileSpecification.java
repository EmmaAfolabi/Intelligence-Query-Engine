package com.example.demo.repository;

import com.example.demo.model.Profile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProfileSpecification {

    public static Specification<Profile> byFilters(
            String gender, String ageGroup, String countryId,
            Integer minAge, Integer maxAge,
            Float minGenderProbability, Float minCountryProbability) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(gender)) {
                predicates.add(cb.equal(cb.lower(root.get("gender")), gender.toLowerCase()));
            }
            if (StringUtils.hasText(ageGroup)) {
                predicates.add(cb.equal(cb.lower(root.get("ageGroup")), ageGroup.toLowerCase()));
            }
            if (StringUtils.hasText(countryId)) {
                predicates.add(cb.equal(cb.upper(root.get("countryId")), countryId.toUpperCase()));
            }
            if (minAge != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("age"), minAge));
            }
            if (maxAge != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("age"), maxAge));
            }
            if (minGenderProbability != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("genderProbability"), minGenderProbability));
            }
            if (minCountryProbability != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("countryProbability"), minCountryProbability));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
