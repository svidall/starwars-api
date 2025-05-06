package com.conexa.service.impl;

import com.conexa.converter.FilmsConverter;
import com.conexa.model.FilmsRawResponse;
import com.conexa.model.FilmsRawResult;
import com.conexa.model.FilmsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class FilmsServiceImplTest {

    @InjectMocks
    private FilmsServiceImpl filmsService;

    @Mock
    private FilmsConverter filmsConverter;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filmsService = new FilmsServiceImpl(filmsConverter);
        setBaseUrl();
        injectRestTemplate(filmsService, restTemplate);
    }

    private void setBaseUrl() {
        try {
            var field = FilmsServiceImpl.class.getDeclaredField("baseUrl");
            field.setAccessible(true);
            field.set(filmsService, "https://swapi.tech/api");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void injectRestTemplate(FilmsServiceImpl service, RestTemplate restTemplate) {
        try {
            var field = FilmsServiceImpl.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(service, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetFilms_success() {
        FilmsRawResult film1 = new FilmsRawResult();
        FilmsRawResult film2 = new FilmsRawResult();
        FilmsRawResult film3 = new FilmsRawResult();

        List<FilmsRawResult> mockList = List.of(film1, film2, film3);
        FilmsRawResponse raw = new FilmsRawResponse();
        raw.setResult(mockList);

        when(restTemplate.getForObject(contains("/films"), eq(FilmsRawResponse.class)))
                .thenReturn(raw);

        FilmsResponse expected = new FilmsResponse();
        when(filmsConverter.convert(any(), anyInt())).thenReturn(expected);

        FilmsResponse response = filmsService.getFilms(1, 2, "A New Hope");
        assertNotNull(response);
        verify(filmsConverter).convert(any(), eq(3));
    }

    @Test
    void testGetFilms_returnsNullWhenResultIsNull() {
        when(restTemplate.getForObject(anyString(), eq(FilmsRawResponse.class)))
                .thenReturn(null);

        assertNull(filmsService.getFilms(1, 2, "A New Hope"));
    }

    @Test
    void testGetFilms_returnsNullWhenPageOutOfRange() {
        FilmsRawResponse raw = new FilmsRawResponse();
        raw.setResult(List.of());

        when(restTemplate.getForObject(anyString(), eq(FilmsRawResponse.class)))
                .thenReturn(raw);

        assertNull(filmsService.getFilms(10, 5, "Some title"));
    }

    @Test
    void testGetFilmById_success() {
        FilmsRawResponse raw = new FilmsRawResponse();
        when(restTemplate.getForObject(contains("/films/1"), eq(FilmsRawResponse.class)))
                .thenReturn(raw);

        FilmsResponse expected = new FilmsResponse();
        when(filmsConverter.convert(any(), any())).thenReturn(expected);

        FilmsResponse response = filmsService.getFilmById(1);
        assertNotNull(response);
        verify(filmsConverter).convert(eq(raw), isNull());
    }

    @Test
    void testGetFilmById_notFound() {
        when(restTemplate.getForObject(anyString(), eq(FilmsRawResponse.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        assertNull(filmsService.getFilmById(999));
    }

    @Test
    void testGetFilms_throwsRuntimeExceptionOnRestError() {
        when(restTemplate.getForObject(anyString(), eq(FilmsRawResponse.class)))
                .thenThrow(new RuntimeException("Connection failed"));

        assertThrows(RuntimeException.class, () -> filmsService.getFilms(1, 2, "title"));
    }
}
