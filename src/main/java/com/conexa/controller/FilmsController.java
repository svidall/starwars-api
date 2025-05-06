package com.conexa.controller;

import com.conexa.model.FilmsResponse;
import com.conexa.service.FilmsService;
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
@RequestMapping("/api/films")
@CrossOrigin("*")
@Tag(name = "Películas", description = "Operaciones relacionadas con la gestión de películas")
@RequiredArgsConstructor
public class FilmsController {

    private final FilmsService filmsService;

    @Operation(
            summary = "Obtener listado de películas",
            description = "Retorna una lista paginada de películas, con opción de filtrar por título.",
            parameters = {
                    @Parameter(name = "page", description = "Número de página a retornar (por defecto: 1)", schema = @Schema(type = "integer", defaultValue = "1")),
                    @Parameter(name = "limit", description = "Cantidad de películas por página (por defecto: 10)", schema = @Schema(type = "integer", defaultValue = "10")),
                    @Parameter(name = "title", description = "Filtrar películas por título (opcional)", schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado de películas obtenido exitosamente",
                            content = @Content(schema = @Schema(implementation = FilmsResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No se encontraron películas")
            }
    )
    @GetMapping
    public ResponseEntity<?> getFilms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String title,
            HttpServletRequest httpServletRequest,
            Principal principal
    ) {
        var films = filmsService.getFilms(page, limit, title);
        if (films == null) {
            return ResponseEntity.notFound().build();
        }
        int totalRecords = films.getPagination().getTotalRecords();
        films.setPagination(PaginateUtil.getPagination(httpServletRequest, page, limit, title, "title", totalRecords));
        return ResponseEntity.ok(films);
    }


    @Operation(
            summary = "Obtener detalles de una película por ID",
            description = "Retorna la información detallada de una película específica utilizando su ID.",
            parameters = {
                    @Parameter(name = "filmId", description = "ID de la película a buscar", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalles de la película obtenidos exitosamente",
                            content = @Content(schema = @Schema(implementation = FilmsResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No se encontró la película con el ID especificado")
            }
    )
    @GetMapping("/{filmId}")
    public ResponseEntity<?> getPerson(@PathVariable int filmId) {
        var response = filmsService.getFilmById(filmId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        response.setPagination(null);
        return ResponseEntity.ok(response);
    }
}