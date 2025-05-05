package com.conexa.controller;

import com.conexa.service.PeopleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.security.Principal;

@RestController
@RequestMapping("/api/people")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PeopleController {
    private final PeopleService starWarsService;

    @GetMapping
    public ResponseEntity<?> getPeople(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String name,
            HttpServletRequest httpServletRequest,
            Principal principal

    ) throws URISyntaxException {
        var response = starWarsService.getPeople(page, limit, name);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        if (response.getPagination().getNext() != null) {
            response.getPagination().setNext(getUri(httpServletRequest, page + 1, limit, name));
        }
        if (response.getPagination().getPrevious() != null) {
            response.getPagination().setNext(getUri(httpServletRequest, page - 1, limit, name));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<?> getPerson(@PathVariable int personId) {
        var response = starWarsService.getPeopleById(personId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        response.setPagination(null);
        return ResponseEntity.ok(response);
    }


    private String getUri(HttpServletRequest httpServletRequest, int page, int limit, String name) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(httpServletRequest.getRequestURI())
                .queryParam("page", page)
                .queryParam("limit", limit);
        if (name != null && !name.isBlank()) {
            builder.queryParam("name", name);
        }
        return builder.toUriString();
    }
}
