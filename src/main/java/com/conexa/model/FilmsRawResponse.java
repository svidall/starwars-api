package com.conexa.model;

import com.conexa.helper.FilmsRawResultDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmsRawResponse {
    private String message;
    @JsonDeserialize(using = FilmsRawResultDeserializer.class)
    private List<FilmsRawResult> result;
}
