package com.conexa.model;

import com.conexa.helper.FilmsRawResultDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilmsRawResponse {
    private String message;
    @JsonDeserialize(using = FilmsRawResultDeserializer.class)
    private List<FilmsRawResult> result;
}
