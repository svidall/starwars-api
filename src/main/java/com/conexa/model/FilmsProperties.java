package com.conexa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class FilmsProperties {
    private OffsetDateTime created;
    private OffsetDateTime edited;
    private List<String> starships;
    private List<String> vehicles;
    private List<String> planets;
    private String producer;
    private String title;
    @JsonProperty("episode_id")
    private long episodeID;
    private String director;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    @JsonProperty("opening_crawl")
    private String openingCrawl;
    private List<String> characters;
    private List<String> species;
    private String url;
}
