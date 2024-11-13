package com.coveo.challenge.features.search.controller;

import com.coveo.challenge.features.search.service.FrontSuggestionsRecord;
import com.coveo.challenge.features.search.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SuggestionsController {
    private final CityService cityServices;

    final Logger logger = LoggerFactory.getLogger(SuggestionsController.class);

    public SuggestionsController(CityService cityServices) {
        this.cityServices = cityServices;
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://search.coveo.com"})
    @PostMapping("/suggestions")
    public FrontSuggestionsRecord suggestions(@RequestBody SuggestionsDtoRecord suggestionsDtoRecord) {
        logger.info("--- Entering suggestions endpoint parameters are: {}", suggestionsDtoRecord);
        return cityServices.retrieveCities(suggestionsDtoRecord.query(),
                suggestionsDtoRecord.filters(),
                suggestionsDtoRecord.pageInfos()
        );
    }
}

