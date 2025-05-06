package com.conexa.controller;

import com.conexa.model.Pagination;
import com.conexa.service.FilmsService;
import com.conexa.util.PaginateUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.security.Principal;

@RestController
@RequestMapping("/api/films")
@CrossOrigin("*")
@RequiredArgsConstructor
public class FilmsController {

    private final FilmsService filmsService;

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
        films.setPagination(PaginateUtil.getPagination(httpServletRequest, page, limit, title,"title", totalRecords));
        return ResponseEntity.ok(films);
    }


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
