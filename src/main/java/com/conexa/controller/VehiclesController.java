package com.conexa.controller;

import com.conexa.service.StarshipsService;
import com.conexa.service.VehiclesService;
import com.conexa.service.impl.VehiclesServiceImpl;
import com.conexa.util.PaginateUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin("*")
@RequiredArgsConstructor
public class VehiclesController {

    private final VehiclesService vehiclesService;

    @GetMapping
    public ResponseEntity<?> getVehicles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String name,
            HttpServletRequest httpServletRequest,
            Principal principal

    ) {
        var response = vehiclesService.getVehicles(page, limit, name);
        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        }
        int totalRecords = response.getPagination().getTotalRecords();
        response.setPagination(PaginateUtil.getPagination(httpServletRequest, page, limit, name, "name", totalRecords));
        if (page > response.getPagination().getTotalPages()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<?> getStarshipById(@PathVariable int vehicleId) {
        var response = vehiclesService.getVehicleById(vehicleId);
        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        }
        response.setPagination(null);
        return ResponseEntity.ok(response);
    }
}
