package com.conexa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VehiclesRawResponse {
    private String message;
    @JsonProperty("total_records")
    private Integer totalRecords;
    @JsonProperty("total_pages")
    private Integer totalPages;
    private String previous;
    private String next;
    private List<VehiclesRawResult> results;
}
