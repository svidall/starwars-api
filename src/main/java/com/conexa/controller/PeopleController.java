package com.conexa.controller;

import com.conexa.model.PeopleResponse;
import com.conexa.service.PeopleService;
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

@RestController
@RequestMapping("/api/people")
@CrossOrigin("*")
@Tag(name = "Personas", description = "Operaciones relacionadas con la gestión de personas")
@RequiredArgsConstructor
public class PeopleController {
    private final PeopleService peopleService;

    @Operation(
            summary = "Obtener listado de personas",
            description = "Retorna una lista paginada de personas, con opción de filtrar por nombre.",
            parameters = {
                    @Parameter(name = "page", description = "Número de página a retornar (por defecto: 1)", schema = @Schema(type = "integer", defaultValue = "1")),
                    @Parameter(name = "limit", description = "Cantidad de personas por página (por defecto: 10)", schema = @Schema(type = "integer", defaultValue = "10")),
                    @Parameter(name = "name", description = "Filtrar personas por nombre (opcional)", schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado de personas obtenido exitosamente",
                            content = @Content(schema = @Schema(implementation = PeopleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No se encontraron personas")
            }
    )
    @GetMapping
    public ResponseEntity<?> getPeople(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String name,
            HttpServletRequest httpServletRequest,
            Principal principal

    ) {
        var response = peopleService.getPeople(page, limit, name);
        if (response == null) {
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
            summary = "Obtener detalles de una persona por ID",
            description = "Retorna la información detallada de una persona específica utilizando su ID.",
            parameters = {
                    @Parameter(name = "personId", description = "ID de la persona a buscar", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalles de la persona obtenidos exitosamente",
                            content = @Content(schema = @Schema(implementation = PeopleResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No se encontró la persona con el ID especificado")
            }
    )
    @GetMapping("/{personId}")
    public ResponseEntity<?> getPerson(@PathVariable int personId) {
        var response = peopleService.getPeopleById(personId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        response.setPagination(null);
        return ResponseEntity.ok(response);
    }

}