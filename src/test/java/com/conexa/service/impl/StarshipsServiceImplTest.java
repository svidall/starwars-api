package com.conexa.service.impl;

import com.conexa.converter.StarshipsConverter;
import com.conexa.model.StarshipsRawResponse;
import com.conexa.model.StarshipsResponse;
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
class StarshipsServiceImplTest {

    @InjectMocks
    private StarshipsServiceImpl starshipsService;

    @Mock
    private StarshipsConverter starshipsConverter;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        starshipsService = new StarshipsServiceImpl(starshipsConverter);
        setBaseUrl("https://swapi.tech/api");
        injectRestTemplate(starshipsService, restTemplate);
    }

    private void setBaseUrl(String url) {
        try {
            var field = StarshipsServiceImpl.class.getDeclaredField("baseUrl");
            field.setAccessible(true);
            field.set(starshipsService, url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void injectRestTemplate(StarshipsServiceImpl service, RestTemplate restTemplate) {
        try {
            var field = StarshipsServiceImpl.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(service, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetStarships_success() {
        // Arrange
        StarshipsRawResponse raw = new StarshipsRawResponse();
        when(restTemplate.getForObject(contains("/starships"), eq(StarshipsRawResponse.class)))
                .thenReturn(raw);

        StarshipsResponse expected = new StarshipsResponse();
        when(starshipsConverter.convert(raw)).thenReturn(expected);

        StarshipsResponse result = starshipsService.getStarships(1, 10, "X-wing");

        assertNotNull(result);
        verify(starshipsConverter).convert(raw);
    }

    @Test
    void testGetStarshipById_success() {
        StarshipsRawResponse raw = new StarshipsRawResponse();
        when(restTemplate.getForObject(contains("/starships/3"), eq(StarshipsRawResponse.class)))
                .thenReturn(raw);

        StarshipsResponse expected = new StarshipsResponse();
        when(starshipsConverter.convert(raw)).thenReturn(expected);

        StarshipsResponse result = starshipsService.getStarshipById(3);

        assertNotNull(result);
        verify(starshipsConverter).convert(raw);
    }

    @Test
    void testGetStarships_notFound() {
        when(restTemplate.getForObject(anyString(), eq(StarshipsRawResponse.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        StarshipsResponse result = starshipsService.getStarships(1, 10, "TIE");
        assertNull(result);
    }

    @Test
    void testGetStarshipById_notFound() {
        when(restTemplate.getForObject(anyString(), eq(StarshipsRawResponse.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        StarshipsResponse result = starshipsService.getStarshipById(999);
        assertNull(result);
    }

    @Test
    void testGetStarships_restClientError() {
        when(restTemplate.getForObject(anyString(), eq(StarshipsRawResponse.class)))
                .thenThrow(new RuntimeException("API unavailable"));

        assertThrows(RuntimeException.class, () -> starshipsService.getStarships(1, 10, "Falcon"));
    }
}
