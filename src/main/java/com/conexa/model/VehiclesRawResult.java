package com.conexa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiclesRawResult {
    private VehiclesProperties properties;
    private String uid;
}
