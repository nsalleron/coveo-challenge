package com.coveo.challenge.features.search.service;

import com.coveo.challenge.features.search.repository.CityRecord;

import java.util.List;

public record FrontSuggestionsRecord(Pagination pagination, List<CityRecord> cities, FrontSuggestionsFilters filters) {
    public record Pagination(Integer page, Integer totalNumberOfPages) {}
    public record FrontSuggestionsFilters(List<FrontFilter> countries, List<FrontFilter> admins) {}
}
