package com.conexa.service;

import com.conexa.model.PeopleResponse;

import java.net.URISyntaxException;

public interface PeopleService {
    PeopleResponse getPeople(int page, int limit, String name);
    PeopleResponse getPeopleById(int id);
}
