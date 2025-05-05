package com.conexa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertiesPeople {
    private OffsetDateTime created;
    private OffsetDateTime edited;
    private String name;
    private String gender;
    @JsonProperty("skin_color")
    private String skinColor;
    @JsonProperty("hair_color")
    private String hairColor;
    private String height;
    @JsonProperty("eye_color")
    private String eyeColor;
    private String mass;
    private String homeworld;
    @JsonProperty("birth_year")
    private String birthYear;
    private String url;
}

