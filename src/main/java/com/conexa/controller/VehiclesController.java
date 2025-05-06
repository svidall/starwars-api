package com.conexa.controller;

import com.conexa.model.VehiclesResponse;
import com.conexa.service.VehiclesService;
import com.conexa.util.PaginateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin("*")
@Tag(name = "Vehículos", description = "Operaciones relacionadas con la gestión de vehículos")
@RequiredArgsConstructor
public class VehiclesController {

    private final VehiclesService vehiclesService;

    @Operation(
            summary = "Obtener listado de vehículos",
            description = "Retorna una lista paginada de vehículos, con opción de filtrar por nombre.",
            parameters = {
                    @Parameter(name = "page", description = "Número de página a retornar (por defecto: 1)", schema = @Schema(type = "integer", defaultValue = "1")),
                    @Parameter(name = "limit", description = "Cantidad de vehículos por página (por defecto: 10)", schema = @Schema(type = "integer", defaultValue = "10")),
                    @Parameter(name = "name", description = "Filtrar vehículos por nombre (opcional)", schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado de vehículos obtenido exitosamente",
                            content = @Content(schema = @Schema(implementation = VehiclesResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No se encontraron vehículos")
            }
    )
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

    @Operation(
            summary = "Obtener detalles de un vehículo por ID",
            description = "Retorna la información detallada de un vehículo específico utilizando su ID.",
            parameters = {
                    @Parameter(name = "vehicleId", description = "ID del vehículo a buscar", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalles del vehículo obtenidos exitosamente",
                            content = @Content(schema = @Schema(implementation = VehiclesResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No se encontró el vehículo con el ID especificado")
            }
    )
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