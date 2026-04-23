package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProfileDto {
    private String name;
    private String gender;
    
    @JsonProperty("gender_probability")
    private Float genderProbability;
    
    private Integer age;
    
    @JsonProperty("age_group")
    private String ageGroup;
    
    @JsonProperty("country_id")
    private String countryId;
    
    @JsonProperty("country_name")
    private String countryName;
    
    @JsonProperty("country_probability")
    private Float countryProbability;
}
