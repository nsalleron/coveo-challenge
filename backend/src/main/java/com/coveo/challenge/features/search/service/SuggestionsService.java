package com.coveo.challenge.features.search.service;

import com.coveo.challenge.core.LatLngUtils;
import com.coveo.challenge.features.search.controller.SuggestionsDtoRecord;
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
public class SuggestionsService {
    public static final int MAX_DEFAULT_RADIUS = 100;
    public static final int MAX_PAGE_SIZE = 5;
    final Logger logger = LoggerFactory.getLogger(SuggestionsService.class);
    private final CityRepository cityRepository;

    public SuggestionsService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
        this.cityRepository.initialize();
    }

    private static List<CityEntity> retrieveSublistOfCities(Integer currentPage, Integer pageSize, List<CityEntity> cities) {
        final int endIndex = Math.min((currentPage * pageSize + pageSize), cities.size());
        final int startIndex = (currentPage * pageSize);
        cities = cities.subList(startIndex, endIndex);
        return cities;
    }

    public FrontSuggestionsRecord retrieveSuggestions(Optional<String> query,
                                                      SuggestionsDtoRecord.Filters filters,
                                                      SuggestionsDtoRecord.PagesInfo pagesInfo) {
        if (query.isEmpty()) {
            return new FrontSuggestionsRecord(new FrontSuggestionsRecord.Pagination(null, null), Collections.emptyList(), new FrontSuggestionsRecord.FrontSuggestionsFilters(Collections.emptyList(), Collections.emptyList()));
        }

        Stream<CityEntity> citiesStream = cityRepository.findByNameContaining(query.get().toLowerCase()).stream();
        citiesStream = filterAccordingToLatLng(filters.geolocation(), citiesStream);
        citiesStream = filterAccordingToSelectedCountries(filters.countries(), citiesStream);
        citiesStream = filterAccordingToSelectedAdmins(filters.admins(), citiesStream);

        List<CityEntity> cities = citiesStream.toList();

        List<FrontFilter> countries = new java.util.ArrayList<>(cities.stream().map((c) -> new FrontFilter(c.getCountry().hashCode(), c.getCountry())).distinct().toList());
        List<FrontFilter> admins = new java.util.ArrayList<>(cities.stream().flatMap((c) -> c.getAdmins().stream().map((a) -> new FrontFilter(a.hashCode(), a))).distinct().filter((c) -> !c.name().isEmpty()).toList());

        return getPaginatedSuggestions(pagesInfo, cities, countries, admins);
    }

    private Stream<CityEntity> filterAccordingToSelectedCountries(Optional<List<String>> selectedCountries, Stream<CityEntity> citiesStream) {
        Stream<CityEntity> filteredCitiesStream = citiesStream;
        if (selectedCountries.isPresent()) {
            final List<String> countries = selectedCountries.get();
            for (String country : countries) {
                filteredCitiesStream = filteredCitiesStream.filter(city -> city.getCountry().equals(country));
            }
        }
        return filteredCitiesStream;
    }

    private Stream<CityEntity> filterAccordingToSelectedAdmins(Optional<List<String>> selectedAdmins, Stream<CityEntity> citiesStream) {
        Stream<CityEntity> filteredCitiesStream = citiesStream;
        if (selectedAdmins.isPresent()) {
            final List<String> admins = selectedAdmins.get();
            for (String admin : admins) {
                filteredCitiesStream = filteredCitiesStream.filter(city -> city.getAdmins().contains(admin));
            }
        }
        return filteredCitiesStream;
    }

    private Stream<CityEntity> filterAccordingToLatLng(Optional<SuggestionsDtoRecord.Filters.Geolocation> geolocation, Stream<CityEntity> citiesStream) {
        Stream<CityEntity> filteredCitiesStream = citiesStream;
        if (geolocation.isPresent()) {
            final SuggestionsDtoRecord.Filters.Geolocation geo = geolocation.get();
            final int radius = geo.radius().map(Math::abs).orElse(MAX_DEFAULT_RADIUS);

            filteredCitiesStream = filteredCitiesStream.filter(city -> LatLngUtils.calculateDistance(city.getLatitude(), city.getLongitude(), geo.latitude(), geo.longitude()) < radius);
        }
        return filteredCitiesStream;
    }

    private FrontSuggestionsRecord getPaginatedSuggestions(SuggestionsDtoRecord.PagesInfo pagesInfo, List<CityEntity> cities, List<FrontFilter> countries, List<FrontFilter> admins) {
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

    private List<FrontCityRecord> getSubListAccordingToCurrentPage(Integer currentPage, List<CityEntity> cities, Integer pageSize) {
        List<CityEntity> paginatedCities;
        if (currentPage < getTotalNumberOfPages(cities, pageSize)) {
            paginatedCities = retrieveSublistOfCities(currentPage, pageSize, cities);
        } else {
            paginatedCities = Collections.emptyList();
            logger.warn("no cities found");
        }

        final int totalNumberOfCities = cities.size();

        return paginatedCities.stream().map((c) -> {
            final float score = (float) (100 - (cities.indexOf(c) * totalNumberOfCities) / 100) / 100;
            return new FrontCityRecord(c,score);
        }).toList();
    }

    private Predicate<CityEntity> fromName(String q) {
        return (c) -> isInAltNames(c.getAltName(), q) || isInName(c.getName(), q);
    }

    private boolean isInAltNames(List<String> altNames, String q) {
        return altNames.stream().filter((alt) -> alt.toLowerCase().contains(q)).toList().size() > 0;
    }

    private boolean isInName(String name, String q) {
        return name.toLowerCase().contains(q);
    }

    private Integer getTotalNumberOfPages(List<CityEntity> cities, Integer pageSize) {

        return cities.size() % pageSize == 0 ? cities.size() / pageSize : (cities.size() / pageSize) + 1;
    }
}
