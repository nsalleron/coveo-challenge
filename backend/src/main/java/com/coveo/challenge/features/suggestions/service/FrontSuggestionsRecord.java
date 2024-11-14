package com.coveo.challenge.features.suggestions.service;

import java.util.List;

public record FrontSuggestionsRecord(Pagination pagination, List<FrontCityRecord> cities,
                                     FrontSuggestionsFilters filters) {
    public record Pagination(Integer page, Integer totalNumberOfPages) {
    }

    public record FrontSuggestionsFilters(List<FrontFilter> countries, List<FrontFilter> admins) {
    }
}
