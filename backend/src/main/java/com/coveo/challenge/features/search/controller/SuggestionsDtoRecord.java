package com.coveo.challenge.features.search.controller;

import java.util.Optional;

public record SuggestionsDtoRecord(Optional<String> query, Optional<Float> latitude, Optional<Float> longitude, Optional<Integer> page, Optional<Integer> radius, Optional<Integer> pageSize){}
