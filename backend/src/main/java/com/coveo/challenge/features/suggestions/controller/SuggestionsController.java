package com.coveo.challenge.features.suggestions.controller;

import com.coveo.challenge.features.suggestions.service.FrontSuggestionsRecord;
import com.coveo.challenge.features.suggestions.service.SuggestionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuggestionsController {
    final Logger logger = LoggerFactory.getLogger(SuggestionsController.class);
    private final SuggestionsService suggestionsService;

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

