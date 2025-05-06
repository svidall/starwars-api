package com.conexa.service.impl;

import com.conexa.converter.StarshipsConverter;
import com.conexa.converter.VehiclesConverter;
import com.conexa.model.StarshipsRawResponse;
import com.conexa.model.StarshipsResponse;
import com.conexa.model.VehiclesRawResponse;
import com.conexa.model.VehiclesResponse;
import com.conexa.service.StarshipsService;
import com.conexa.service.VehiclesService;
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
public class VehiclesServiceImpl implements VehiclesService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final VehiclesConverter vehiclesConverter;
    @Value("${swapi.baseurl}")
    private String baseUrl;
    private final String STARSHIPS_URI = "/starships";

    @Override
    public VehiclesResponse getVehicles(int page, int limit, String name) {
        String url = UriComponentsBuilder.fromUriString(baseUrl).path(STARSHIPS_URI)
                .queryParam("expanded", "true")
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("name", name)
                .toUriString();
        return getStarshipsResponse(url);
    }

    @Override
    public VehiclesResponse getVehicleById(int id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl).path(STARSHIPS_URI + "/" + id).toUriString();
        return getStarshipsResponse(url);
    }

    private VehiclesResponse getStarshipsResponse(String url) {
        try {
            var rawResponse = restTemplate.getForObject(url, VehiclesRawResponse.class);
            if (Objects.isNull(rawResponse)) {
                return null;
            }
            return vehiclesConverter.convert(rawResponse);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new RuntimeException("Error al consumir la API de SWAPI", e);
        }
    }

}
