package com.conexa.converter;

import com.conexa.model.Pagination;
import com.conexa.model.PeopleRawResponse;
import com.conexa.model.PeopleResponse;
import com.conexa.model.PeopleResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PeopleConverter {
    public PeopleResponse convert(PeopleRawResponse raw) {
        List<PeopleResult> converted = raw.getResults().stream()
                .map(r -> {
                    PeopleResult rp = new PeopleResult();
                    rp.setId(r.getUid());
                    rp.setPerson(r.getProperties());
                    return rp;
                })
                .toList();

        PeopleResponse response = new PeopleResponse();
        Pagination pagination = new Pagination(raw.getTotalRecords(), raw.getTotalPages(), raw.getPrevious(), raw.getNext());
        response.setPagination(pagination);
        response.setData(converted);
        return response;
    }

}
