package com.conexa.controller;

import com.conexa.model.Pagination;
import com.conexa.model.StarshipsResponse;
import com.conexa.service.StarshipsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class StarshipsControllerTest {

    @InjectMocks
    private StarshipsController starshipsController;

    @Mock
    private StarshipsService starshipsService;

    @Test
    void getStarships_shouldReturnOkWithStarshipsResponse() {
        // Arrange
        int page = 1;
        int limit = 10;
        String name = "Millennium Falcon";
        MockHttpServletRequest request = new MockHttpServletRequest();
        StarshipsResponse mockResponse = new StarshipsResponse();
        mockResponse.setData(new ArrayList<>());
        Pagination pagination = new Pagination();
        pagination.setTotalRecords(1);
        mockResponse.setPagination(pagination);

        when(starshipsService.getStarships(page, limit, name)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = starshipsController.getStarships(page, limit, name, request, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getStarships_shouldReturnNotFoundWhenStarshipsServiceReturnsNull() {
        int page = 1;
        int limit = 10;
        String name = "NonExistentStarship";
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(starshipsService.getStarships(page, limit, name)).thenReturn(null);

        ResponseEntity<?> responseEntity = starshipsController.getStarships(page, limit, name, request, null);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getStarships_shouldReturnNotFoundWhenPageIsGreaterThanTotalPages() {
        int page = 2;
        int limit = 10;
        String name = "X-Wing";
        MockHttpServletRequest request = new MockHttpServletRequest();
        StarshipsResponse mockResponse = new StarshipsResponse();
        mockResponse.setData(new ArrayList<>());
        Pagination pagination = new Pagination();
        pagination.setTotalRecords(1);
        mockResponse.setPagination(pagination);

        when(starshipsService.getStarships(page, limit, name)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = starshipsController.getStarships(page, limit, name, request, null);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getStarshipById_shouldReturnOkWithStarshipsResponseWithoutPagination() {
        int starshipId = 1;
        StarshipsResponse mockResponse = new StarshipsResponse();
        mockResponse.setData(new ArrayList<>());
        mockResponse.setPagination(new Pagination());

        when(starshipsService.getStarshipById(starshipId)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = starshipsController.getStarshipById(starshipId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        StarshipsResponse starshipsResponse = (StarshipsResponse) responseEntity.getBody();
        assert starshipsResponse != null;
        assertNull(starshipsResponse.getPagination());
    }

    @Test
    void getStarshipById_shouldReturnNotFoundWhenStarshipsServiceReturnsNull() {
        int starshipId = 99;

        when(starshipsService.getStarshipById(starshipId)).thenReturn(null);

        ResponseEntity<?> responseEntity = starshipsController.getStarshipById(starshipId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}