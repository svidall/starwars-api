package com.conexa.converter;

import com.conexa.model.Pagination;
import com.conexa.model.PeopleResponse;
import com.conexa.model.RawPeopleResponse;
import com.conexa.model.ResultPeople;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PeopleConverter {
    public PeopleResponse convert(RawPeopleResponse raw) {
        ResultPeople[] converted = Arrays.stream(raw.getResults())
                .map(r -> {
                    ResultPeople rp = new ResultPeople();
                    rp.setId(r.getUid());
                    rp.setPerson(r.getProperties());
                    return rp;
                })
                .toArray(ResultPeople[]::new);

        PeopleResponse response = new PeopleResponse();
        Pagination pagination = new Pagination(raw.getTotalRecords(), raw.getTotalPages(), raw.getPrevious(), raw.getNext());
        response.setPagination(pagination);
        response.setData(converted);
        return response;
    }

}
