package com.conexa.converter;

import com.conexa.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StarshipsConverter {
    public StarshipsResponse convert(StarshipsRawResponse raw) {
        List<StarshipsResult> converted = raw.getResults().stream()
                .map(r -> {
                    StarshipsResult rp = new StarshipsResult();
                    rp.setId(r.getUid());
                    rp.setStarship(r.getProperties());
                    return rp;
                })
                .toList();

        StarshipsResponse response = new StarshipsResponse();
        Pagination pagination = new Pagination(raw.getTotalRecords(), raw.getTotalPages(), raw.getPrevious(), raw.getNext());
        response.setPagination(pagination);
        response.setData(converted);
        return response;
    }

}
