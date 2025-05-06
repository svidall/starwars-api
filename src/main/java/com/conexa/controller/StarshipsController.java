package com.conexa.controller;

import com.conexa.service.FilmsService;
import com.conexa.service.PeopleService;
import com.conexa.service.StarshipsService;
import com.conexa.service.impl.StarshipsServiceImpl;
import com.conexa.util.PaginateUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/starships")
@CrossOrigin("*")
@RequiredArgsConstructor
public class StarshipsController {
    private final StarshipsService starshipsService;

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
