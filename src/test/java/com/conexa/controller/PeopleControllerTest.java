package com.conexa.controller;

import com.conexa.model.Pagination;
import com.conexa.model.PeopleResponse;
import com.conexa.service.PeopleService;
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
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PeopleControllerTest {

    @InjectMocks
    private PeopleController peopleController;

    @Mock
    private PeopleService peopleService;

    @Test
    void getPeople_shouldReturnOkWithPeopleResponse() {
        // Arrange
        int page = 1;
        int limit = 10;
        String name = "Test Name";
        MockHttpServletRequest request = new MockHttpServletRequest();
        PeopleResponse mockResponse = new PeopleResponse();
        mockResponse.setData(new ArrayList<>());
        mockResponse.setPagination(new Pagination());
        mockResponse.getPagination().setTotalRecords(1);

        when(peopleService.getPeople(page, limit, name)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = peopleController.getPeople(page, limit, name, request, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getPeople_shouldReturnNotFoundWhenPeopleServiceReturnsNull() {
        int page = 1;
        int limit = 10;
        String name = "NonExistentName";
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(peopleService.getPeople(page, limit, name)).thenReturn(null);

        ResponseEntity<?> responseEntity = peopleController.getPeople(page, limit, name, request, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getPeople_shouldReturnNotFoundWhenPageIsGreaterThanTotalPages() {

        int page = 2;
        int limit = 10;
        String name = "Test Name";
        MockHttpServletRequest request = new MockHttpServletRequest();
        PeopleResponse mockResponse = new PeopleResponse();
        mockResponse.setData(new ArrayList<>());
        Pagination pagination = new Pagination();
        pagination.setTotalRecords(1);
        mockResponse.setPagination(pagination);

        when(peopleService.getPeople(page, limit, name)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = peopleController.getPeople(page, limit, name, request, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getPerson_shouldReturnOkWithPeopleResponseWithoutPagination() {
        int personId = 456;
        PeopleResponse mockResponse = new PeopleResponse();
        mockResponse.setData(new ArrayList<>());
        mockResponse.setPagination(new Pagination());

        when(peopleService.getPeopleById(personId)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = peopleController.getPerson(personId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getPerson_shouldReturnNotFoundWhenPeopleServiceReturnsNull() {
        int personId = 789;

        when(peopleService.getPeopleById(personId)).thenReturn(null);

        ResponseEntity<?> responseEntity = peopleController.getPerson(personId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}