package com.coveo.challenge.features.search.controller;

import com.coveo.challenge.features.search.service.FrontSuggestionsRecord;
import com.coveo.challenge.features.search.service.SuggestionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class SuggestionsController {
    private final SuggestionsService suggestionsService;

    final Logger logger = LoggerFactory.getLogger(SuggestionsController.class);

    public SuggestionsController(SuggestionsService suggestionsServices) {
        this.suggestionsService = suggestionsServices;
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://search.coveo.com"})
    @PostMapping("/suggestions")
    public FrontSuggestionsRecord suggestions(@RequestBody SuggestionsDtoRecord suggestionsDtoRecord) {
        logger.info("--- Entering suggestions endpoint parameters are: {}", suggestionsDtoRecord);
        return suggestionsService.retrieveSuggestions(suggestionsDtoRecord.query(),
                suggestionsDtoRecord.filters(),
                suggestionsDtoRecord.pageInfos()
        );
    }
}

