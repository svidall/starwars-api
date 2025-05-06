package com.conexa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilmsResponse {
    private String message;
    private Pagination pagination;
    private List<FilmsResult> data;

}
