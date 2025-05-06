package com.conexa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiclesProperties {
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
    @JsonProperty("vehicle_class")
    private String vehicleClass;
    private List<String> pilots;
    private List<String> films;
    private String url;

}

