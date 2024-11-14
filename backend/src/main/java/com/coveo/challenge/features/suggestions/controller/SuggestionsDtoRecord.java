package com.coveo.challenge.features.suggestions.controller;

import java.util.List;
import java.util.Optional;

public record SuggestionsDtoRecord(Optional<String> query, Filters filters, PagesInfo pageInfos) {
    public record Filters(Optional<Geolocation> geolocation, Optional<List<String>> countries,
                          Optional<List<String>> admins) {
        public record Geolocation(Float latitude, Float longitude, Optional<Integer> radius) {
        }
    }

    public record PagesInfo(Optional<Integer> page, Optional<Integer> pageSize) {
    }
}