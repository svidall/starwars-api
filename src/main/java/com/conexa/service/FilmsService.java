package com.conexa.service;

import com.conexa.model.FilmsResponse;

public interface FilmsService {
    FilmsResponse getFilms(int page, int limit, String title);
    FilmsResponse getFilmById(int id);
}
