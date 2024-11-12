package com.coveo.challenge.features.search.service;

import com.coveo.challenge.core.LatLngUtils;
import com.coveo.challenge.features.search.repository.CityRecord;
import com.coveo.challenge.features.search.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class CityService {
    public static final int MAX_DEFAULT_RADIUS = 100;
    public static final int MAX_PAGE_SIZE = 5;

    private final CityRepository cityRepository;

    final Logger logger = LoggerFactory.getLogger(CityService.class);

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    private Integer retrieveCurrentPage(Optional<Integer> page) {
        return page.map(integer -> integer < 0 ? Integer.valueOf(0) : integer).orElse(null);
    }

    public FrontSuggestionsRecord retrieveCities(Optional<String> query,
                                                 Optional<Integer> page,
                                                 Optional<Float> latitude,
                                                 Optional<Float> longitude,
                                                 Optional<Integer> radius,
                                                 Optional<Integer> pageSize,
                                                 Optional<String> country) {

        if (query.isEmpty()) {
            return new FrontSuggestionsRecord(null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        }

        Integer currentPage = retrieveCurrentPage(page);
        Integer currentRadius = retrieveCurrentRadius(radius);
        Integer currentpageSize = retrieveCurrentPageSize(pageSize);


        Stream<CityRecord> citiesStream = cityRepository.getCities().stream().filter(fromName(query.get().toLowerCase()));
        citiesStream = filterAccordingToLatLng(latitude, longitude, currentRadius, citiesStream);

        if (country.isPresent()) {
            citiesStream = citiesStream.filter(city -> city.country().equals(country.get()));
        }

        List<CityRecord> cities = citiesStream.toList();

        List<FrontFilter> countries = new java.util.ArrayList<>(cities.stream().map((c) -> new FrontFilter(c.country().hashCode(), c.country())).distinct().toList());
        List<FrontFilter> admins = new java.util.ArrayList<>(cities.stream().flatMap((c) -> c.admins().stream().map((a) -> new FrontFilter(a.hashCode(), a))).distinct().filter((c) -> !c.name().isEmpty()).toList());

        return page.isPresent() ? getPaginatedSuggestions(currentPage, currentpageSize, cities, countries, admins) : new FrontSuggestionsRecord(null, null, cities, countries, admins);
    }

    private Integer retrieveCurrentPageSize(Optional<Integer> pageSize) {
        if (pageSize.isPresent()) {
            if (pageSize.get() <= 0) {
                return MAX_PAGE_SIZE;
            }
            return pageSize.get();
        }
        return MAX_PAGE_SIZE;
    }

    private Integer retrieveCurrentRadius(Optional<Integer> radius) {
        return radius.map(Math::abs).orElse(MAX_DEFAULT_RADIUS);
    }

    private static Stream<CityRecord> filterAccordingToLatLng(Optional<Float> latitude, Optional<Float> longitude, Integer radius, Stream<CityRecord> citiesStream) {
        if (latitude.isPresent() && longitude.isPresent()) {
            citiesStream = citiesStream.filter(city -> LatLngUtils.calculateDistance(city.latitude(), city.longitude(), latitude.get(), longitude.get()) < radius);
        }
        return citiesStream;
    }

    private FrontSuggestionsRecord getPaginatedSuggestions(Integer page, Integer pageSize, List<CityRecord> cities, List<FrontFilter> countries, List<FrontFilter> admins) {
        final Integer currentPage = page < 0 ? 0 : page;
        return new FrontSuggestionsRecord(
                currentPage,
                getTotalNumberOfPages(cities, pageSize),
                getSubListAccordingToCurrentPage(currentPage, cities, pageSize),
                countries,
                admins
        );
    }

    private List<CityRecord> getSubListAccordingToCurrentPage(Integer currentPage, List<CityRecord> cities, Integer pageSize) {
        if (currentPage < getTotalNumberOfPages(cities, pageSize)) {
            cities = retrieveSublistOfCities(currentPage, pageSize, cities);
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

    private Integer getTotalNumberOfPages(List<CityRecord> cities, Integer pageSize) {

        return cities.size() % pageSize == 0 ? cities.size() / pageSize : (cities.size() / pageSize) + 1;
    }

    private static List<CityRecord> retrieveSublistOfCities(Integer currentPage, Integer pageSize, List<CityRecord> cities) {
        final int endIndex = Math.min((currentPage * pageSize + pageSize), cities.size());
        final int startIndex = (currentPage * pageSize);
        cities = cities.subList(startIndex, endIndex);
        return cities;
    }
}
