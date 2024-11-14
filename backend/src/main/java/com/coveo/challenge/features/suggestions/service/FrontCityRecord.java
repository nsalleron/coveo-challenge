package com.coveo.challenge.features.suggestions.service;

import com.coveo.challenge.features.suggestions.repository.CityRecord;

public record FrontCityRecord(CityRecord city, float score) {
}
