package com.coveo.challenge.features.search;

import com.coveo.challenge.features.search.repository.CityRecord;

import java.util.List;

public record FrontSuggestionsRecord(Integer page, Integer totalNumberOfPages, List<CityRecord> cities) {}
