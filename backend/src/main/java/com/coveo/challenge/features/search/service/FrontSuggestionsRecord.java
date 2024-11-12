package com.coveo.challenge.features.search.service;

import com.coveo.challenge.features.search.repository.CityRecord;

import java.util.List;

public record FrontSuggestionsRecord(Integer page, Integer totalNumberOfPages, List<CityRecord> cities, List<FrontFilter> countries, List<FrontFilter> admins) {}
