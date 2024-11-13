/**
 * Copyright (c) 2011 - 2019, Coveo Solutions Inc.
 */
package com.coveo.challenge.suggestion;

import com.coveo.challenge.features.search.controller.SuggestionsDtoRecord;
import com.coveo.challenge.features.search.service.CityService;
import com.coveo.challenge.features.search.service.FrontSuggestionsRecord;
import com.coveo.challenge.features.search.controller.SuggestionsController;
import com.coveo.challenge.suggestion.helper.SuggestionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SuggestionsControllerTest {

    @Autowired
    private SuggestionsController controller;

    @MockBean
    private CityService cityService;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void givenSearchInfoThenSuggestionsShouldCorrectlyBePassed() {
        final Integer currentPage = 0;
        final SuggestionsDtoRecord.Filters filters = new SuggestionsDtoRecord.Filters(
                Optional.of(new SuggestionsDtoRecord.Filters.Geolocation(1.0F, 2.0F, Optional.empty())),
                Optional.of(Collections.emptyList()),
                Optional.of(Collections.emptyList()));
        final SuggestionsDtoRecord.PagesInfo pagesInfo = new SuggestionsDtoRecord.PagesInfo(Optional.of(currentPage), Optional.of(5));


        final FrontSuggestionsRecord result = new FrontSuggestionsRecord(
                new FrontSuggestionsRecord.Pagination(0, 3),
                SuggestionHelper.CITIES,
                new FrontSuggestionsRecord.FrontSuggestionsFilters(Collections.emptyList(), Collections.emptyList()));

        when(cityService.retrieveCities(Optional.of("Qu"), filters, pagesInfo)).thenReturn(result);

        final FrontSuggestionsRecord searchResult = controller.suggestions(new SuggestionsDtoRecord(Optional.of("Qu"), filters, pagesInfo));

        assertEquals(currentPage, searchResult.pagination().page());

        verify(cityService).retrieveCities(Optional.of("Qu"), filters, pagesInfo);

    }
}
