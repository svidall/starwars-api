package com.conexa.service.impl;

import com.conexa.converter.FilmsConverter;
import com.conexa.model.FilmsRawResponse;
import com.conexa.model.FilmsRawResult;
import com.conexa.model.FilmsResponse;
import com.conexa.service.FilmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmsServiceImpl implements FilmsService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final FilmsConverter filmsConverter;
    @Value("${swapi.baseurl}")
    private String baseUrl;
    private final String FILMS_URI = "/films";

    @Override
    public FilmsResponse getFilms(int page, int limit, String title) {
        String url = UriComponentsBuilder.fromUriString(baseUrl).path(FILMS_URI)
                .queryParam("title", title)
                .toUriString();

        try {
            var rawResponse = restTemplate.getForObject(url, FilmsRawResponse.class);
            if (rawResponse == null) {
                return null;
            }
            int total = rawResponse.getResult().size();
            int fromIndex = page * limit - limit;
            if (fromIndex > total) {
                return null;
            }
            int toIndex = Math.min(fromIndex + limit, total);
            rawResponse.setResult(rawResponse.getResult().subList(fromIndex, toIndex));
            return filmsConverter.convert(rawResponse, total);

        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new RuntimeException("Error al consumir la API de SWAPI", e);
        }
    }

    @Override
    public FilmsResponse getFilmById(int id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl).path(FILMS_URI + "/" + id).toUriString();
        try {
            var rawResponse = restTemplate.getForObject(url, FilmsRawResponse.class);
            return filmsConverter.convert(Objects.requireNonNull(rawResponse), null);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new RuntimeException("Error al consumir la API de SWAPI", e);
        }
    }


}
