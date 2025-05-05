package com.conexa.service;

import com.conexa.model.PeopleResponse;

import java.net.URISyntaxException;

public interface PeopleService {
    PeopleResponse getPeople(int page, int limit, String name) throws URISyntaxException;
    PeopleResponse getPeopleById(int id);
}
