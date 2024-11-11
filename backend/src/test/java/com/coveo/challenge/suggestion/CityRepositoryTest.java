/**
 * Copyright (c) 2011 - 2019, Coveo Solutions Inc.
 */
package com.coveo.challenge.suggestion;

import com.coveo.challenge.features.search.repository.CityRecord;
import com.coveo.challenge.features.search.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CityRepositoryTest {

    private CityRepository repository;

    @MockBean
    private ResourceLoader resourceLoader;

    @Test
    void contextLoads() {
        assertThat(repository).isNotNull();
    }

    @BeforeEach
    void setUp() throws IOException {
        when(resourceLoader.getResource("classpath:data/cities_canada-usa.tsv")).thenReturn(new ClassPathResource("data/cities_canada-usa_subset.tsv"));
        repository = new CityRepository(resourceLoader);
    }

    @Test
    public void givenSubsetOfCitiesWhenGetCitiesCalledThenWeShouldHaveCorrectNumberOfCities() {
        final List<CityRecord> records = repository.getCities();

        assertEquals(2, records.size());
        verify(resourceLoader, times(1)).getResource(anyString());

    }



}
