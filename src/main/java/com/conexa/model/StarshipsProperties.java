package com.conexa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class StarshipsProperties {
    private OffsetDateTime created;
    private OffsetDateTime edited;
    private String consumables;
    private String name;
    @JsonProperty("cargo_capacity")
    private String cargoCapacity;
    private String passengers;
    @JsonProperty("max_atmosphering_speed")
    private String maxAtmospheringSpeed;
    private String crew;
    private String length;
    private String model;
    @JsonProperty("cost_in_credits")
    private String costInCredits;
    private String manufacturer;
    private List<String> pilots;
    @JsonProperty("MGLT")
    private String mglt;
    @JsonProperty("starship_class")
    private String starshipClass;
    @JsonProperty("hyperdrive_rating")
    private String hyperdriveRating;
    private List<String> films;
    private String url;
}
