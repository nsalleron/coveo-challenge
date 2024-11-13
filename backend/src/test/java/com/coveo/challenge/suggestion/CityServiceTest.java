/**
 * Copyright (c) 2011 - 2019, Coveo Solutions Inc.
 */
package com.coveo.challenge.suggestion;

import com.coveo.challenge.features.search.controller.SuggestionsDtoRecord;
import com.coveo.challenge.features.search.service.FrontSuggestionsRecord;
import com.coveo.challenge.features.search.repository.CityRecord;
import com.coveo.challenge.features.search.repository.CityRepository;
import com.coveo.challenge.features.search.service.CityService;
import com.coveo.challenge.suggestion.helper.SuggestionHelper;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CityServiceTest {

    @Autowired
    private CityService service;

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


        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qu"), filters, pagesInfo);

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

        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qué"), filters, pagesInfo);

        assertEquals(1, searchResult.pagination().totalNumberOfPages());
        assertEquals(searchResult.cities(), List.of(SuggestionHelper.QUEBEC_CITY));
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

        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qu"), filters, pagesInfo);

        assertEquals(SuggestionHelper.CITIES, searchResult.cities());
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

        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qu"), filters, pagesInfo);

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


        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qu"), filters, pagesInfo);


        assertEquals(0, searchResult.pagination().page());
        assertEquals(1, searchResult.pagination().totalNumberOfPages());
        assertEquals(searchResult.cities(), List.of(SuggestionHelper.QUEBEC_CITY));
    }
}
