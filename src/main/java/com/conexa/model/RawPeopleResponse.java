package com.conexa.model;

import com.conexa.util.RawResultPeopleDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawPeopleResponse {
    private String message;
    @JsonProperty("total_records")
    private Integer totalRecords;
    @JsonProperty("total_pages")
    private Integer totalPages;
    private String previous;
    private String next;
    @JsonAlias({"results", "result"})
    @JsonDeserialize(using = RawResultPeopleDeserializer.class)
    private RawResultPeople[] results;
}
