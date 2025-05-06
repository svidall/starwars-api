package com.conexa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pagination {
    @JsonProperty("total_records")
    private Integer totalRecords;
    @JsonProperty("total_pages")
    private Integer totalPages;
    private String previous;
    private String next;
}
