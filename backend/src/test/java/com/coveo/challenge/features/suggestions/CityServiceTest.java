/**
 * Copyright (c) 2011 - 2019, Coveo Solutions Inc.
 */
package com.coveo.challenge.features.suggestions;

import com.coveo.challenge.features.suggestions.controller.SuggestionsDtoRecord;
import com.coveo.challenge.features.suggestions.helper.SuggestionHelper;
import com.coveo.challenge.features.suggestions.repository.CityRecord;
import com.coveo.challenge.features.suggestions.repository.CityRepository;
import com.coveo.challenge.features.suggestions.service.FrontSuggestionsRecord;
import com.coveo.challenge.features.suggestions.service.SuggestionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CityServiceTest {

    @Autowired
    private SuggestionsService service;

    @MockBean
    private CityRepository cityRepository;

    @Test
    void contextLoads() {
        assertThat(service).isNotNull();
    }

    @Test
    public void givenUnknownCityThenSuggestionsShouldBeEmpty() {
        final FrontSuggestionsRecord expectedSuggestions = new FrontSuggestionsRecord(
                new FrontSuggestionsRecord.Pagination(0, 0),
                Collections.emptyList(),
                new FrontSuggestionsRecord.FrontSuggestionsFilters(Collections.emptyList(), Collections.emptyList()));

        final SuggestionsDtoRecord.Filters filters = new SuggestionsDtoRecord.Filters(
                Optional.empty(),
                Optional.of(Collections.emptyList()),
                Optional.of(Collections.emptyList()));
        final SuggestionsDtoRecord.PagesInfo pagesInfo = new SuggestionsDtoRecord.PagesInfo(Optional.of(0), Optional.of(5));


        final FrontSuggestionsRecord searchResult = service.retrieveSuggestions(Optional.of("Qu"), filters, pagesInfo);

        assertEquals(expectedSuggestions, searchResult);
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenSearchThenWeShouldOnlyOneResult() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);
        final SuggestionsDtoRecord.Filters filters = new SuggestionsDtoRecord.Filters(
                Optional.empty(),
                Optional.of(Collections.emptyList()),
                Optional.of(Collections.emptyList()));
        final SuggestionsDtoRecord.PagesInfo pagesInfo = new SuggestionsDtoRecord.PagesInfo(Optional.of(0), Optional.of(5));

        final FrontSuggestionsRecord searchResult = service.retrieveSuggestions(Optional.of("Qué"), filters, pagesInfo);

        assertEquals(1, searchResult.pagination().totalNumberOfPages());
        assertEquals(searchResult.cities(), List.of(SuggestionHelper.FRONT_QUEBEC_CITY));
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenSearchThenWeShouldMultipleResult() {
        final List<CityRecord> bigCitiesList = Stream.concat(SuggestionHelper.CITIES.stream(), SuggestionHelper.CITIES.stream()).toList();
        when(cityRepository.getCities()).thenReturn(bigCitiesList);
        final SuggestionsDtoRecord.Filters filters = new SuggestionsDtoRecord.Filters(
                Optional.empty(),
                Optional.of(Collections.emptyList()),
                Optional.of(Collections.emptyList()));
        final SuggestionsDtoRecord.PagesInfo pagesInfo = new SuggestionsDtoRecord.PagesInfo(Optional.of(0), Optional.of(5));

        final FrontSuggestionsRecord searchResult = service.retrieveSuggestions(Optional.of("Qu"), filters, pagesInfo);

        assertEquals(SuggestionHelper.FRONT_CITIES, searchResult.cities());
        assertEquals(2, searchResult.pagination().totalNumberOfPages());
        verify(cityRepository, times(1)).getCities();
    }

    @Test
    public void givenSearchWithLatLngThenWeShouldHaveEmptyResult() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);
        final SuggestionsDtoRecord.Filters filters = new SuggestionsDtoRecord.Filters(
                Optional.of(new SuggestionsDtoRecord.Filters.Geolocation(1.0F, 2.0F, Optional.of(100))),
                Optional.of(Collections.emptyList()),
                Optional.of(Collections.emptyList()));
        final SuggestionsDtoRecord.PagesInfo pagesInfo = new SuggestionsDtoRecord.PagesInfo(Optional.of(0), Optional.of(5));

        final FrontSuggestionsRecord searchResult = service.retrieveSuggestions(Optional.of("Qu"), filters, pagesInfo);

        assertEquals(Collections.emptyList(), searchResult.cities());
        assertEquals(0, searchResult.pagination().totalNumberOfPages());
        verify(cityRepository, times(1)).getCities();
    }


    @Test
    public void givenNegativePageNumberCityThenSuggestionsShouldStartAtPageZero() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);
        final SuggestionsDtoRecord.Filters filters = new SuggestionsDtoRecord.Filters(
                Optional.of(new SuggestionsDtoRecord.Filters.Geolocation(46.81228F, -71.21454F, Optional.of(100))),
                Optional.of(Collections.emptyList()),
                Optional.of(Collections.emptyList()));
        final SuggestionsDtoRecord.PagesInfo pagesInfo = new SuggestionsDtoRecord.PagesInfo(Optional.of(Integer.MIN_VALUE), Optional.of(5));


        final FrontSuggestionsRecord searchResult = service.retrieveSuggestions(Optional.of("Qu"), filters, pagesInfo);


        assertEquals(0, searchResult.pagination().page());
        assertEquals(1, searchResult.pagination().totalNumberOfPages());
        assertEquals(searchResult.cities(), List.of(SuggestionHelper.FRONT_QUEBEC_CITY));
    }
}
