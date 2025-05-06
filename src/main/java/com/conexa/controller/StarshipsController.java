package com.conexa.controller;

import com.conexa.model.StarshipsResponse;
import com.conexa.service.StarshipsService;
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
@RequestMapping("/api/starships")
@CrossOrigin("*")
@Tag(name = "Naves Espaciales", description = "Operaciones relacionadas con la gestión de naves espaciales")
@RequiredArgsConstructor
public class StarshipsController {
    private final StarshipsService starshipsService;

    @Operation(
            summary = "Obtener listado de naves espaciales",
            description = "Retorna una lista paginada de naves espaciales, con opción de filtrar por nombre.",
            parameters = {
                    @Parameter(name = "page", description = "Número de página a retornar (por defecto: 1)", schema = @Schema(type = "integer", defaultValue = "1")),
                    @Parameter(name = "limit", description = "Cantidad de naves espaciales por página (por defecto: 10)", schema = @Schema(type = "integer", defaultValue = "10")),
                    @Parameter(name = "name", description = "Filtrar naves espaciales por nombre (opcional)", schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado de naves espaciales obtenido exitosamente",
                            content = @Content(schema = @Schema(implementation = StarshipsResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No se encontraron naves espaciales")
            }
    )
    @GetMapping
    public ResponseEntity<?> getStarships(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String name,
            HttpServletRequest httpServletRequest,
            Principal principal

    ) {
        var response = starshipsService.getStarships(page, limit, name);
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
            summary = "Obtener detalles de una nave espacial por ID",
            description = "Retorna la información detallada de una nave espacial específica utilizando su ID.",
            parameters = {
                    @Parameter(name = "starshipId", description = "ID de la nave espacial a buscar", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalles de la nave espacial obtenidos exitosamente",
                            content = @Content(schema = @Schema(implementation = StarshipsResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No se encontró la nave espacial con el ID especificado")
            }
    )
    @GetMapping("/{starshipId}")
    public ResponseEntity<?> getStarshipById(@PathVariable int starshipId) {
        var response = starshipsService.getStarshipById(starshipId);
        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        }
        response.setPagination(null);
        return ResponseEntity.ok(response);
    }
}