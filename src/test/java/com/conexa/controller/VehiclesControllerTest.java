package com.conexa.controller;

import com.conexa.model.Pagination;
import com.conexa.model.VehiclesResponse;
import com.conexa.service.VehiclesService;
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
class VehiclesControllerTest {

    @InjectMocks
    private VehiclesController vehiclesController;

    @Mock
    private VehiclesService vehiclesService;

    @Test
    void getVehicles_shouldReturnOkWithVehiclesResponse() {
        // Arrange
        int page = 1;
        int limit = 10;
        String name = "Sand Crawler";
        MockHttpServletRequest request = new MockHttpServletRequest();
        VehiclesResponse mockResponse = new VehiclesResponse();
        mockResponse.setData(new ArrayList<>());
        Pagination pagination = new Pagination();
        pagination.setTotalRecords(1);
        mockResponse.setPagination(pagination);

        when(vehiclesService.getVehicles(page, limit, name)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = vehiclesController.getVehicles(page, limit, name, request, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        VehiclesResponse vehiclesResponse = (VehiclesResponse) responseEntity.getBody();
        assert vehiclesResponse != null;
        assertEquals(0, vehiclesResponse.getData().size());
    }

    @Test
    void getVehicles_shouldReturnNotFoundWhenVehiclesServiceReturnsNull() {
        int page = 1;
        int limit = 10;
        String name = "NonExistentVehicle";
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(vehiclesService.getVehicles(page, limit, name)).thenReturn(null);

        ResponseEntity<?> responseEntity = vehiclesController.getVehicles(page, limit, name, request, null);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getVehicles_shouldReturnNotFoundWhenPageIsGreaterThanTotalPages() {
        int page = 2;
        int limit = 10;
        String name = "TIE fighter";
        MockHttpServletRequest request = new MockHttpServletRequest();
        VehiclesResponse mockResponse = new VehiclesResponse();
        mockResponse.setData(new ArrayList<>());
        Pagination pagination = new Pagination();
        pagination.setTotalRecords(1);
        mockResponse.setPagination(pagination);

        when(vehiclesService.getVehicles(page, limit, name)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = vehiclesController.getVehicles(page, limit, name, request, null);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getStarshipById_shouldReturnOkWithVehiclesResponseWithoutPagination() {
        int vehicleId = 1;
        VehiclesResponse mockResponse = new VehiclesResponse();
        mockResponse.setData(new ArrayList<>());
        mockResponse.setPagination(new Pagination());

        when(vehiclesService.getVehicleById(vehicleId)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = vehiclesController.getStarshipById(vehicleId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        VehiclesResponse vehiclesResponse = (VehiclesResponse) responseEntity.getBody();
        assert vehiclesResponse != null;
        assertNull(vehiclesResponse.getPagination());
    }

    @Test
    void getStarshipById_shouldReturnNotFoundWhenVehiclesServiceReturnsNull() {
        int vehicleId = 99;

        when(vehiclesService.getVehicleById(vehicleId)).thenReturn(null);

        ResponseEntity<?> responseEntity = vehiclesController.getStarshipById(vehicleId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}