package com.conexa.converter;

import com.conexa.model.Pagination;
import com.conexa.model.VehiclesRawResponse;
import com.conexa.model.VehiclesResponse;
import com.conexa.model.VehiclesResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehiclesConverter {
    public VehiclesResponse convert(VehiclesRawResponse raw) {
        List<VehiclesResult> converted = raw.getResults().stream()
                .map(r -> {
                    VehiclesResult rp = new VehiclesResult();
                    rp.setId(r.getUid());
                    rp.setVehicle(r.getProperties());
                    return rp;
                })
                .toList();

        VehiclesResponse response = new VehiclesResponse();
        Pagination pagination = new Pagination(raw.getTotalRecords(), raw.getTotalPages(), raw.getPrevious(), raw.getNext());
        response.setPagination(pagination);
        response.setData(converted);
        return response;
    }

}
