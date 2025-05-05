package com.conexa.service.impl;

import com.conexa.converter.PeopleConverter;
import com.conexa.model.PeopleResponse;
import com.conexa.model.RawPeopleResponse;
import com.conexa.service.PeopleService;
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
public class PeopleServiceImpl implements PeopleService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final PeopleConverter peopleConverter;
    @Value("${swapi.baseurl}")
    private String baseUrl;
    private final String PEOPLE_URI = "/people";

    @Override
    public PeopleResponse getPeople(int page, int limit, String name) {
        String url = UriComponentsBuilder.fromUriString(baseUrl).path(PEOPLE_URI)
                .queryParam("expanded", "true")
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("name", name)
                .toUriString();

        try {
            var rawResponse = restTemplate.getForObject(url, RawPeopleResponse.class);
            return peopleConverter.convert(Objects.requireNonNull(rawResponse));
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new RuntimeException("Error al consumir la API de SWAPI", e);
        }
    }

    @Override
    public PeopleResponse getPeopleById(int id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl).path(PEOPLE_URI + "/" + id).toUriString();
        try {
            var rawResponse = restTemplate.getForObject(url, RawPeopleResponse.class);
            return peopleConverter.convert(Objects.requireNonNull(rawResponse));
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new RuntimeException("Error al consumir la API de SWAPI", e);
        }
    }


}
