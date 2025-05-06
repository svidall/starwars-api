package com.conexa.service;

import com.conexa.model.PeopleResponse;

public interface PeopleService {
    PeopleResponse getPeople(int page, int limit, String name);
    PeopleResponse getPeopleById(int id);
}
