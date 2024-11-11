/**
 * Copyright (c) 2011 - 2019, Coveo Solutions Inc.
 */
package com.coveo.challenge.suggestion;

import com.coveo.challenge.features.search.repository.CityRecord;
import com.coveo.challenge.features.search.repository.CityRepository;
import com.coveo.challenge.features.search.service.CityService;
import com.coveo.challenge.features.search.FrontSuggestionsRecord;
import com.coveo.challenge.suggestion.helper.SuggestionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        final FrontSuggestionsRecord expectedSuggestions = new FrontSuggestionsRecord(0, 0, new ArrayList<>());

        final FrontSuggestionsRecord searchResult = service.retrieveCities("test", 0, null, null);

        assertEquals(expectedSuggestions, searchResult);
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenSearchThenWeShouldOnlyOneResult() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);
        final FrontSuggestionsRecord searchResult = service.retrieveCities("Qué", 0, null, null);

        assertEquals(1, searchResult.totalNumberOfPages());
        assertEquals(searchResult.cities(), List.of(SuggestionHelper.QUEBEC_CITY));
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenSearchThenWeShouldMultipleResult() {
        final List<CityRecord> bigCitiesList = Stream.concat(SuggestionHelper.CITIES.stream(), SuggestionHelper.CITIES.stream()).toList();
        when(cityRepository.getCities()).thenReturn(bigCitiesList);
        final FrontSuggestionsRecord searchResult = service.retrieveCities("Qu", 0, null, null);

        assertEquals(SuggestionHelper.CITIES, searchResult.cities());
        assertEquals(2, searchResult.totalNumberOfPages());
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenSearchWithLatLngThenWeShouldHaveEmptyResult() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);
        final FrontSuggestionsRecord searchResult = service.retrieveCities("Qu", 0, 1.0F, 2.0F);

        assertEquals(Collections.emptyList(), searchResult.cities());
        assertEquals(0, searchResult.totalNumberOfPages());
        verify(cityRepository, times(1)).getCities();

    }

    @Test
    public void givenSearchWithoutPageThenWeShouldHaveNullTotalNumberOfPages() {
        when(cityRepository.getCities()).thenReturn(SuggestionHelper.CITIES);
        final FrontSuggestionsRecord searchResult = service.retrieveCities("Qu", null, 1.0F, 2.0F);

        assertEquals(Collections.emptyList(), searchResult.cities());
        assertNull(searchResult.totalNumberOfPages());
        assertNull(searchResult.page());
        verify(cityRepository, times(1)).getCities();

    }
}
