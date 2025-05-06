package com.conexa.service;

import com.conexa.model.VehiclesResponse;

public interface VehiclesService {
    VehiclesResponse getVehicles(int page, int limit, String name);
    VehiclesResponse getVehicleById(int id);
}
