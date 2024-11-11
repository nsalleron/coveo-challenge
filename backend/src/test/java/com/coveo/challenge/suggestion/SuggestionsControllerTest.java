/**
 * Copyright (c) 2011 - 2019, Coveo Solutions Inc.
 */
package com.coveo.challenge.suggestion;

import com.coveo.challenge.features.search.service.CityService;
import com.coveo.challenge.features.search.FrontSuggestionsRecord;
import com.coveo.challenge.features.search.controller.SuggestionsController;
import com.coveo.challenge.suggestion.helper.SuggestionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SuggestionsControllerTest {

    @Autowired
    private SuggestionsController controller; //DI

    @MockBean
    private CityService cityService;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void givenNegativePageNumberCityThenSuggestionsShouldStartAtPageZero() {
        final Integer currentPage = Integer.MIN_VALUE;
        when(cityService.retrieveCities("Qu", 0, null, null)).thenReturn(new FrontSuggestionsRecord(0, 1, SuggestionHelper.CITIES));

        final FrontSuggestionsRecord searchResult = controller.suggestions("Qu", null, null, currentPage);

        assertEquals(0, searchResult.page());
        assertEquals(1, searchResult.totalNumberOfPages());
        assertEquals(searchResult.cities(), SuggestionHelper.CITIES);
        verify(cityService, times(1)).retrieveCities("Qu", 0, null, null);
    }

    @Test
    public void givenSearchInfoThenSuggestionsShouldCorrectlyBePassed() {
        final Integer currentPage = Integer.MAX_VALUE;
        when(cityService.retrieveCities("Qu", currentPage, 1.0F, 2.0F)).thenReturn(new FrontSuggestionsRecord(currentPage, 3, SuggestionHelper.CITIES));

        final FrontSuggestionsRecord searchResult = controller.suggestions("Qu", 1.0F, 2.0F, currentPage);

        assertEquals(Integer.MAX_VALUE, searchResult.page());
        verify(cityService)
                .retrieveCities("Qu", Integer.MAX_VALUE, 1.0F, 2.0F );
    }
}
