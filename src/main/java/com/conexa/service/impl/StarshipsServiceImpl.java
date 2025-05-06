package com.conexa.service.impl;

import com.conexa.converter.PeopleConverter;
import com.conexa.converter.StarshipsConverter;
import com.conexa.model.PeopleRawResponse;
import com.conexa.model.StarshipsRawResponse;
import com.conexa.model.StarshipsResponse;
import com.conexa.service.StarshipsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StarshipsServiceImpl implements StarshipsService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final StarshipsConverter starshipsConverter;
    @Value("${swapi.baseurl}")
    private String baseUrl;
    private final String STARSHIPS_URI = "/starships";

    @Override
    public StarshipsResponse getStarships(int page, int limit, String name) {
        String url = UriComponentsBuilder.fromUriString(baseUrl).path(STARSHIPS_URI)
                .queryParam("expanded", "true")
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("name", name)
                .toUriString();
        return getStarshipsResponse(url);
    }

    @Override
    public StarshipsResponse getStarshipById(int id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl).path(STARSHIPS_URI + "/" + id).toUriString();
        return getStarshipsResponse(url);
    }

    private StarshipsResponse getStarshipsResponse(String url) {
        try {
            var rawResponse = restTemplate.getForObject(url, StarshipsRawResponse.class);
            if (Objects.isNull(rawResponse)) {
                return null;
            }
            return starshipsConverter.convert(rawResponse);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new RuntimeException("Error al consumir la API de SWAPI", e);
        }
    }


}
