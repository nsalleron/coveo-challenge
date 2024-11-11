package com.coveo.challenge.features.search.service;

import com.coveo.challenge.features.search.FrontSuggestionsRecord;
import com.coveo.challenge.features.search.repository.CityRecord;
import com.coveo.challenge.features.search.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class CityService {
    public static final int LONGITUDE_THRESH = 20;
    public static final int LATITUDE_THRESH = 10;
    public static final int MAX_PAGE_SIZE = 5;

    private final CityRepository cityRepository;

    final Logger logger = LoggerFactory.getLogger(CityService.class);

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public FrontSuggestionsRecord retrieveCities(String q, Integer page, Float latitude, Float longitude) {
        Stream<CityRecord> citiesStream = cityRepository.getCities().stream().filter(fromName(q.toLowerCase()));
        citiesStream = filterAccordingToLatLng(latitude, longitude, citiesStream);
        List<CityRecord> cities = citiesStream.toList();

        final boolean isPaginated = page != null;
        return isPaginated ? getPaginatedSuggestions(page, cities) : new FrontSuggestionsRecord(null, null, cities);
    }

    private static Stream<CityRecord> filterAccordingToLatLng(Float latitude, Float longitude, Stream<CityRecord> citiesStream) {
        if (latitude != null)
            citiesStream = citiesStream.filter(city -> Math.abs(city.latitude() - latitude) < LATITUDE_THRESH);
        if (longitude != null)
            citiesStream = citiesStream.filter(city -> Math.abs(city.longitude() - longitude) < LONGITUDE_THRESH);
        return citiesStream;
    }

    private FrontSuggestionsRecord getPaginatedSuggestions(Integer page, List<CityRecord> cities) {
        final Integer currentPage = page < 0 ? 0 : page;
        return new FrontSuggestionsRecord(
                currentPage,
                getTotalNumberOfPages(cities),
                getSubListAccordingToCurrentPage(currentPage, cities)
        );
    }

    private List<CityRecord> getSubListAccordingToCurrentPage(Integer currentPage, List<CityRecord> cities) {
        if (currentPage < getTotalNumberOfPages(cities)) {
            cities = retrieveSublistOfCities(currentPage, cities);
        } else {
            cities = Collections.emptyList();
            logger.warn("no cities found");
        }
        return cities;
    }

    private Predicate<CityRecord> fromName(String q) {
        return (c) -> isInAltNames(c.altNames(), q) || isInName(c.name(), q);
    }

    private boolean isInAltNames(String[] altNames, String q) {
        return Arrays.stream(altNames).filter((alt) -> alt.toLowerCase().contains(q)).toList().size() > 0;
    }

    private boolean isInName(String name, String q) {
        return name.toLowerCase().contains(q);
    }

    private Integer getTotalNumberOfPages(List<CityRecord> cities) {
        return cities.size() % MAX_PAGE_SIZE == 0 ? cities.size() / MAX_PAGE_SIZE : (cities.size() / MAX_PAGE_SIZE) + 1;
    }

    private static List<CityRecord> retrieveSublistOfCities(Integer currentPage, List<CityRecord> cities) {
        final int endIndex = Math.min((currentPage * MAX_PAGE_SIZE + MAX_PAGE_SIZE), cities.size());
        final int startIndex = (currentPage * MAX_PAGE_SIZE);
        cities = cities.subList(startIndex, endIndex);
        return cities;
    }
}
