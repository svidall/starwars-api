package com.conexa.controller;

import com.conexa.model.FilmsResponse;
import com.conexa.model.Pagination;
import com.conexa.service.FilmsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FilmsControllerTest {

    @InjectMocks
    private FilmsController filmsController;

    @Mock
    private FilmsService filmsService;

    @Test
    void getFilms_shouldReturnOkWithFilmsResponse() {
        int page = 1;
        int limit = 10;
        String title = "Test Title";
        MockHttpServletRequest request = new MockHttpServletRequest();
        FilmsResponse mockResponse = new FilmsResponse();
        mockResponse.setData(new ArrayList<>());
        mockResponse.setPagination(new Pagination(0, null, null, null));

        when(filmsService.getFilms(page, limit, title)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = filmsController.getFilms(page, limit, title, request, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getFilms_shouldReturnNotFoundWhenFilmsServiceReturnsNull() {
        // Arrange
        int page = 1;
        int limit = 10;
        String title = "NonExistentTitle";
        MockHttpServletRequest request = new MockHttpServletRequest();
        Principal principal = null;

        when(filmsService.getFilms(page, limit, title)).thenReturn(null);

        ResponseEntity<?> responseEntity = filmsController.getFilms(page, limit, title, request, principal);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getPerson_shouldReturnOkWithFilmsResponseWithoutPagination() {
        int filmId = 123;
        FilmsResponse mockResponse = new FilmsResponse();
        mockResponse.setData(new ArrayList<>());
        mockResponse.setPagination(new Pagination());

        when(filmsService.getFilmById(filmId)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = filmsController.getPerson(filmId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        FilmsResponse filmsResponse = (FilmsResponse) responseEntity.getBody();
        assertNull(filmsResponse.getPagination());
    }

    @Test
    void getPerson_shouldReturnNotFoundWhenFilmsServiceReturnsNull() {
        int filmId = 456;

        when(filmsService.getFilmById(filmId)).thenReturn(null);

        ResponseEntity<?> responseEntity = filmsController.getPerson(filmId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}