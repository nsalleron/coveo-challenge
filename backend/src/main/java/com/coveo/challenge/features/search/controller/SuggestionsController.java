package com.coveo.challenge.features.search.controller;

import com.coveo.challenge.features.search.FrontSuggestionsRecord;
import com.coveo.challenge.features.search.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuggestionsController {


    public static final String LAT_PRETTY_LAKE = "45.9778182";
    public static final String LONG_PRETTY_LAKE = "-77.8968753";
    private final CityService cityServices;

    final Logger logger = LoggerFactory.getLogger(SuggestionsController.class);

    public SuggestionsController(CityService cityServices) {
        this.cityServices = cityServices;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/suggestions")
    public FrontSuggestionsRecord suggestions(@RequestParam String q,
                                              @RequestParam(defaultValue = LAT_PRETTY_LAKE, required = false) Float latitude,
                                              @RequestParam(defaultValue = LONG_PRETTY_LAKE, required = false) Float longitude,
                                              @RequestParam(required = false) Integer page) {
        logger.info("--- Entering suggestions endpoint parameters are: q={}, latitude={}, longitude={}, page={}", q, latitude, longitude, page);

        Integer currentPage = page != null && page < 0 ? Integer.valueOf(0) : page;
        return cityServices.retrieveCities(q, currentPage, latitude, longitude);
    }
}
