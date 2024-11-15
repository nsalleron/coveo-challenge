package com.coveo.challenge.features.suggestions.repository;

import java.util.List;

public record CityRecord(
        Integer id,
        String name,
        String country,
        String[] altNames,
        Float latitude,
        Float longitude,
        List<String> admins
) {
}

