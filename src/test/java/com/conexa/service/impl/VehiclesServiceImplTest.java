package com.conexa.service.impl;

import com.conexa.converter.VehiclesConverter;
import com.conexa.model.VehiclesRawResponse;
import com.conexa.model.VehiclesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class VehiclesServiceImplTest {

    @InjectMocks
    private VehiclesServiceImpl vehiclesService;

    @Mock
    private VehiclesConverter vehiclesConverter;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehiclesService = new VehiclesServiceImpl(vehiclesConverter);
        setBaseUrl("https://swapi.tech/api");
        injectRestTemplate(vehiclesService, restTemplate);
    }

    private void setBaseUrl(String url) {
        try {
            var field = VehiclesServiceImpl.class.getDeclaredField("baseUrl");
            field.setAccessible(true);
            field.set(vehiclesService, url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void injectRestTemplate(VehiclesServiceImpl service, RestTemplate restTemplate) {
        try {
            var field = VehiclesServiceImpl.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(service, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetVehicles_success() {
        VehiclesRawResponse raw = new VehiclesRawResponse();
        when(restTemplate.getForObject(contains("/vehicles"), eq(VehiclesRawResponse.class)))
                .thenReturn(raw);

        VehiclesResponse expected = new VehiclesResponse();
        when(vehiclesConverter.convert(raw)).thenReturn(expected);

        VehiclesResponse result = vehiclesService.getVehicles(1, 10, "speeder");

        assertNotNull(result);
        verify(vehiclesConverter).convert(raw);
    }

    @Test
    void testGetVehicleById_success() {
        VehiclesRawResponse raw = new VehiclesRawResponse();
        when(restTemplate.getForObject(contains("/vehicles/5"), eq(VehiclesRawResponse.class)))
                .thenReturn(raw);

        VehiclesResponse expected = new VehiclesResponse();
        when(vehiclesConverter.convert(raw)).thenReturn(expected);

        VehiclesResponse result = vehiclesService.getVehicleById(5);

        assertNotNull(result);
        verify(vehiclesConverter).convert(raw);
    }

    @Test
    void testGetVehicles_notFound() {
        when(restTemplate.getForObject(anyString(), eq(VehiclesRawResponse.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        VehiclesResponse result = vehiclesService.getVehicles(1, 10, "tank");
        assertNull(result);
    }

    @Test
    void testGetVehicleById_notFound() {
        when(restTemplate.getForObject(anyString(), eq(VehiclesRawResponse.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        VehiclesResponse result = vehiclesService.getVehicleById(404);
        assertNull(result);
    }

    @Test
    void testGetVehicles_restClientError() {
        when(restTemplate.getForObject(anyString(), eq(VehiclesRawResponse.class)))
                .thenThrow(new RuntimeException("API failed"));

        assertThrows(RuntimeException.class, () -> vehiclesService.getVehicles(1, 10, "crawler"));
    }
}
