package com.coveo.challenge.suggestion.helper;

import com.coveo.challenge.features.search.repository.CityRecord;

import java.util.List;

public class SuggestionHelper {

    public static final CityRecord QUEBEC_CITY = new CityRecord(6325494, "Québec", new String[]{"Quebec"}, 46.81228F, -71.21454F);
    public static List<CityRecord> CITIES = List.of(
            new CityRecord(4948247, "Quincy", new String[]{"Quincy"}, 42.25288F, -71.00227F),
            QUEBEC_CITY,
            new CityRecord(5310193, "Queen Creek", new String[]{"Queen Creek"}, 33.24866F, -111.6343F),
            new CityRecord(6115187, "Quesnel", new String[]{"Quesnel"}, 52.97842F, -122.4931F),
            new CityRecord(6115355, "Quinte West", new String[]{"Quinte West"}, 44.18342F, -77.56618F)
    );
}
