package com.conexa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiclesResult {
    private VehiclesProperties vehicle;
    private String id;
}
