/**
 * Copyright (c) 2011 - 2019, Coveo Solutions Inc.
 */
package com.coveo.challenge.suggestion;

import com.coveo.challenge.features.search.service.FrontSuggestionsRecord;
import com.coveo.challenge.features.search.repository.CityRecord;
import com.coveo.challenge.features.search.repository.CityRepository;
import com.coveo.challenge.features.search.service.CityService;
import com.coveo.challenge.suggestion.helper.SuggestionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
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
    private CityService service; //DI

    @MockBean
    private CityRepository cityRepository;

    @Test
    void contextLoads() {
        assertThat(service).isNotNull();
    }

    @Test
    public void givenUnknownCityThenSuggestionsShouldBeEmpty() {
        final FrontSuggestionsRecord expectedSuggestions = new FrontSuggestionsRecord(0, 0, new ArrayList<>(), new ArrayList<>());

        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qu"), Optional.of(0), Optional.empty(), Optional.empty(), Optional.of(100), Optional.of(5),Optional.empty());

        assertEquals(expectedSuggestions, searchResult);
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenSearchThenWeShouldOnlyOneResult() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);
        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qué"), Optional.of(0), Optional.empty(), Optional.empty(), Optional.of(100), Optional.of(5),Optional.empty());

        assertEquals(1, searchResult.totalNumberOfPages());
        assertEquals(searchResult.cities(), List.of(SuggestionHelper.QUEBEC_CITY));
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenSearchThenWeShouldMultipleResult() {
        final List<CityRecord> bigCitiesList = Stream.concat(SuggestionHelper.CITIES.stream(), SuggestionHelper.CITIES.stream()).toList();
        when(cityRepository.getCities()).thenReturn(bigCitiesList);
        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qu"), Optional.of(0), Optional.empty(), Optional.empty(), Optional.of(100), Optional.of(5),Optional.empty());

        assertEquals(SuggestionHelper.CITIES, searchResult.cities());
        assertEquals(2, searchResult.totalNumberOfPages());
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenSearchWithLatLngThenWeShouldHaveEmptyResult() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);
        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qu"), Optional.of(0), Optional.of(1.0F), Optional.of(2.0F), Optional.of(100), Optional.of(5),Optional.empty());

        assertEquals(Collections.emptyList(), searchResult.cities());
        assertEquals(0, searchResult.totalNumberOfPages());
        verify(cityRepository, times(1)).getCities();
    }

    @Test
    public void givenSearchWithoutPageThenWeShouldHaveNullTotalNumberOfPages() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);
        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qu"), Optional.empty(), Optional.of(1.0F), Optional.of(2.0F), Optional.of(100), Optional.of(5),Optional.empty());

        assertEquals(Collections.emptyList(), searchResult.cities());
        assertNull(searchResult.totalNumberOfPages());
        assertNull(searchResult.page());
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenNegativePageNumberCityThenSuggestionsShouldStartAtPageZero() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);

        final FrontSuggestionsRecord searchResult = service.retrieveCities(Optional.of("Qu"), Optional.of(Integer.MIN_VALUE), Optional.of(46.81228F), Optional.of(-71.21454F), Optional.of(100), Optional.of(5),Optional.empty());

        assertEquals(0, searchResult.page());
        assertEquals(1, searchResult.totalNumberOfPages());
        assertEquals(searchResult.cities(), List.of(SuggestionHelper.QUEBEC_CITY));
    }
}
