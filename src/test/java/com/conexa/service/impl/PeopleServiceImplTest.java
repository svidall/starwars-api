package com.conexa.service.impl;

import com.conexa.converter.PeopleConverter;
import com.conexa.model.PeopleRawResponse;
import com.conexa.model.PeopleResponse;
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
class PeopleServiceImplTest {

    @InjectMocks
    private PeopleServiceImpl peopleService;

    @Mock
    private PeopleConverter peopleConverter;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        peopleService = new PeopleServiceImpl(peopleConverter);
        setBaseUrl();
        injectRestTemplate(peopleService, restTemplate);
    }

    private void setBaseUrl() {
        try {
            var field = PeopleServiceImpl.class.getDeclaredField("baseUrl");
            field.setAccessible(true);
            field.set(peopleService, "https://swapi.tech/api");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void injectRestTemplate(PeopleServiceImpl service, RestTemplate restTemplate) {
        try {
            var field = PeopleServiceImpl.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(service, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetPeople_success() {
        PeopleRawResponse rawResponse = new PeopleRawResponse();
        when(restTemplate.getForObject(contains("/people"), eq(PeopleRawResponse.class)))
                .thenReturn(rawResponse);

        PeopleResponse expected = new PeopleResponse();
        when(peopleConverter.convert(rawResponse)).thenReturn(expected);

        PeopleResponse response = peopleService.getPeople(1, 10, "Luke");

        assertNotNull(response);
        verify(restTemplate).getForObject(contains("/people"), eq(PeopleRawResponse.class));
        verify(peopleConverter).convert(rawResponse);
    }

    @Test
    void testGetPeople_notFound() {
        when(restTemplate.getForObject(anyString(), eq(PeopleRawResponse.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        PeopleResponse response = peopleService.getPeople(1, 10, "Leia");
        assertNull(response);
    }

    @Test
    void testGetPeople_restClientError() {
        when(restTemplate.getForObject(anyString(), eq(PeopleRawResponse.class)))
                .thenThrow(new RuntimeException("Connection failed"));

        assertThrows(RuntimeException.class, () -> peopleService.getPeople(1, 10, "Han"));
    }

    @Test
    void testGetPeopleById_success() {
        PeopleRawResponse rawResponse = new PeopleRawResponse();
        when(restTemplate.getForObject(contains("/people/1"), eq(PeopleRawResponse.class)))
                .thenReturn(rawResponse);

        PeopleResponse expected = new PeopleResponse();
        when(peopleConverter.convert(rawResponse)).thenReturn(expected);

        PeopleResponse response = peopleService.getPeopleById(1);

        assertNotNull(response);
        verify(peopleConverter).convert(rawResponse);
    }

    @Test
    void testGetPeopleById_notFound() {
        when(restTemplate.getForObject(anyString(), eq(PeopleRawResponse.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        PeopleResponse response = peopleService.getPeopleById(999);
        assertNull(response);
    }

    @Test
    void testGetPeopleById_restClientError() {
        when(restTemplate.getForObject(anyString(), eq(PeopleRawResponse.class)))
                .thenThrow(new RuntimeException("SWAPI down"));

        assertThrows(RuntimeException.class, () -> peopleService.getPeopleById(5));
    }
}
