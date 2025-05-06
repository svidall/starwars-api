package com.conexa.converter;

import com.conexa.model.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FilmsConverter {
    public FilmsResponse convert(FilmsRawResponse raw, Integer totalRecords) {
        List<FilmsResult> converted = raw.getResult().stream()
                .map(r -> {
                    FilmsResult rp = new FilmsResult();
                    rp.setId(r.getUid());
                    rp.setFilm(r.getProperties());
                    return rp;
                })
                .toList();

        FilmsResponse response = new FilmsResponse();
        Pagination pagination = new Pagination(totalRecords, null, null, null);
        response.setPagination(pagination);
        response.setData(converted);
        return response;
    }

}
