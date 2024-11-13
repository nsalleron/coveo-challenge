package com.coveo.challenge.features.search.service;

import com.coveo.challenge.features.search.repository.CityRecord;

public record FrontCityRecord (CityRecord city, float score) {
}
