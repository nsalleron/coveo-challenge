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

import java.util.Optional;

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
    public void givenSearchInfoThenSuggestionsShouldCorrectlyBePassed() {
        final Integer currentPage = Integer.MAX_VALUE;
        when(cityService.retrieveCities(Optional.of("Qu"), Optional.of(Integer.MAX_VALUE), Optional.of(1.0F), Optional.of(2.0F), Optional.of(100), Optional.of(5))).thenReturn(new FrontSuggestionsRecord(currentPage, 3, SuggestionHelper.CITIES));

        final FrontSuggestionsRecord searchResult = controller.suggestions(new SuggestionsDtoRecord(Optional.of("Qu"), Optional.of(1.0F), Optional.of(2.0F), Optional.of(currentPage), Optional.of(100), Optional.of(5)));

        assertEquals(Integer.MAX_VALUE, searchResult.page());

        verify(cityService).retrieveCities(Optional.of("Qu"), Optional.of(Integer.MAX_VALUE), Optional.of(1.0F), Optional.of(2.0F), Optional.of(100), Optional.of(5));

    }
}
