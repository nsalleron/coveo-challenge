package com.coveo.challenge.features.search.service;

import com.coveo.challenge.core.LatLngUtils;
import com.coveo.challenge.features.search.controller.SuggestionsDtoRecord;
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
    final Logger logger = LoggerFactory.getLogger(CityService.class);
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    private static List<CityRecord> retrieveSublistOfCities(Integer currentPage, Integer pageSize, List<CityRecord> cities) {
        final int endIndex = Math.min((currentPage * pageSize + pageSize), cities.size());
        final int startIndex = (currentPage * pageSize);
        cities = cities.subList(startIndex, endIndex);
        return cities;
    }

    public FrontSuggestionsRecord retrieveCities(Optional<String> query,
                                                 SuggestionsDtoRecord.Filters filters,
                                                 SuggestionsDtoRecord.PagesInfo pagesInfo) {
        if (query.isEmpty()) {
            return new FrontSuggestionsRecord(new FrontSuggestionsRecord.Pagination(null, null), Collections.emptyList(), new FrontSuggestionsRecord.FrontSuggestionsFilters(Collections.emptyList(), Collections.emptyList()));
        }

        Stream<CityRecord> citiesStream = cityRepository.getCities().stream().filter(fromName(query.get().toLowerCase()));
        citiesStream = filterAccordingToLatLng(filters.geolocation(), citiesStream);
        citiesStream = filterAccordingToSelectedCountries(filters.countries(), citiesStream);
        citiesStream = filterAccordingToSelectedAdmins(filters.admins(), citiesStream);

        List<CityRecord> cities = citiesStream.toList();

        List<FrontFilter> countries = new java.util.ArrayList<>(cities.stream().map((c) -> new FrontFilter(c.country().hashCode(), c.country())).distinct().toList());
        List<FrontFilter> admins = new java.util.ArrayList<>(cities.stream().flatMap((c) -> c.admins().stream().map((a) -> new FrontFilter(a.hashCode(), a))).distinct().filter((c) -> !c.name().isEmpty()).toList());

        return getPaginatedSuggestions(pagesInfo, cities, countries, admins);
    }

    private Stream<CityRecord> filterAccordingToSelectedCountries(Optional<List<String>> selectedCountries, Stream<CityRecord> citiesStream) {
        Stream<CityRecord> filteredCitiesStream = citiesStream;
        if (selectedCountries.isPresent()) {
            final List<String> countries = selectedCountries.get();
            for (String country : countries) {
                filteredCitiesStream = filteredCitiesStream.filter(city -> city.country().equals(country));
            }
        }
        return filteredCitiesStream;
    }

    private Stream<CityRecord> filterAccordingToSelectedAdmins(Optional<List<String>> selectedAdmins, Stream<CityRecord> citiesStream) {
        Stream<CityRecord> filteredCitiesStream = citiesStream;
        if (selectedAdmins.isPresent()) {
            final List<String> admins = selectedAdmins.get();
            for (String admin : admins) {
                filteredCitiesStream = filteredCitiesStream.filter(city -> city.admins().contains(admin));
            }
        }
        return filteredCitiesStream;
    }

    private Stream<CityRecord> filterAccordingToLatLng(Optional<SuggestionsDtoRecord.Filters.Geolocation> geolocation, Stream<CityRecord> citiesStream) {
        Stream<CityRecord> filteredCitiesStream = citiesStream;
        if (geolocation.isPresent()) {
            final SuggestionsDtoRecord.Filters.Geolocation geo = geolocation.get();
            final int radius = geo.radius().map(Math::abs).orElse(MAX_DEFAULT_RADIUS);

            filteredCitiesStream = filteredCitiesStream.filter(city -> LatLngUtils.calculateDistance(city.latitude(), city.longitude(), geo.latitude(), geo.longitude()) < radius);
        }
        return filteredCitiesStream;
    }

    private FrontSuggestionsRecord getPaginatedSuggestions(SuggestionsDtoRecord.PagesInfo pagesInfo, List<CityRecord> cities, List<FrontFilter> countries, List<FrontFilter> admins) {
        Integer currentPage = retrieveCurrentPage(pagesInfo);
        Integer currentPageSize = retrieveCurrentPageSize(pagesInfo);

        return new FrontSuggestionsRecord(
                new FrontSuggestionsRecord.Pagination(currentPage, getTotalNumberOfPages(cities, currentPageSize)),
                getSubListAccordingToCurrentPage(currentPage, cities, currentPageSize),
                new FrontSuggestionsRecord.FrontSuggestionsFilters(countries, admins)
        );
    }

    private int retrieveCurrentPage(SuggestionsDtoRecord.PagesInfo pageInfo) {
        final int page = pageInfo.page().isPresent() ? pageInfo.page().get() : 0;

        return Math.max(page, 0);
    }

    private Integer retrieveCurrentPageSize(SuggestionsDtoRecord.PagesInfo pagesInfo) {
        final Optional<Integer> pageSize = pagesInfo.pageSize();

        return pageSize.filter(integer -> integer > 0).orElse(MAX_PAGE_SIZE);
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
}
