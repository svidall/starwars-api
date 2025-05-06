package com.conexa.service;

import com.conexa.model.FilmsResponse;
import com.conexa.model.StarshipsResponse;

public interface StarshipsService {
    StarshipsResponse getStarships(int page, int limit, String name);
    StarshipsResponse getStarshipById(int id);
}
