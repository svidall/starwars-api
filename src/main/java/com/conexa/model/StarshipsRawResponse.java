package com.conexa.model;

import lombok.Data;

import java.util.List;

@Data
public class StarshipsRawResponse {
    private String message;
    private Integer totalRecords;
    private Integer totalPages;
    private String previous;
    private String next;
    private List<StarshipsRawResult> results;
}
