package com.conexa.model;

import com.conexa.helper.StarshipsRawResultDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
public class StarshipsRawResponse {
    private String message;
    private Integer totalRecords;
    private Integer totalPages;
    private String previous;
    private String next;
    @JsonAlias({"results", "result"})
    @JsonDeserialize(using = StarshipsRawResultDeserializer.class)
    private List<StarshipsRawResult> results;
}
