package com.coveo.challenge.suggestion.helper;

import com.coveo.challenge.features.search.repository.CityRecord;
import com.coveo.challenge.features.search.service.FrontCityRecord;

import java.util.Collections;
import java.util.List;

public class SuggestionHelper {
    public static final CityRecord QUEBEC_CITY = new CityRecord(6325494, "Québec", "America/Montreal", new String[]{"Quebec"}, 46.81228F, -71.21454F, Collections.emptyList());
    public static final FrontCityRecord FRONT_QUEBEC_CITY = new FrontCityRecord(QUEBEC_CITY, 1F);

    private static final String[] quincy = new String[]{"Quincy"};
    private static final String[] qc = new String[]{"Queen Creek"};
    private static final String[] ql = new String[]{"Quesnel"};
    private static final String[] qw = new String[]{"Quinte West"};
    
    public static List<CityRecord> CITIES = List.of(
            new CityRecord(4948247, "Quincy", "America/New York", quincy, 42.25288F, -71.00227F, Collections.emptyList()),
            QUEBEC_CITY,
            new CityRecord(5310193, "Queen Creek", "America/Phoenix", qc, 33.24866F, -111.6343F, Collections.emptyList()),
            new CityRecord(6115187, "Quesnel", "America/Vancouver", ql, 52.97842F, -122.4931F, Collections.emptyList()),
            new CityRecord(6115355, "Quinte West", "America/Toronto", qw, 44.18342F, -77.56618F, Collections.emptyList())
    );

    public static List<FrontCityRecord> FRONT_CITIES = List.of(
            new FrontCityRecord(new CityRecord(4948247, "Quincy", "America/New York", quincy, 42.25288F, -71.00227F, Collections.emptyList()), 1F),
            FRONT_QUEBEC_CITY,
            new FrontCityRecord(new CityRecord(5310193, "Queen Creek", "America/Phoenix", qc, 33.24866F, -111.6343F, Collections.emptyList()), 1F),
            new FrontCityRecord(new CityRecord(6115187, "Quesnel", "America/Vancouver",ql, 52.97842F, -122.4931F, Collections.emptyList()), 1F),
            new FrontCityRecord(new CityRecord(6115355, "Quinte West", "America/Toronto", qw, 44.18342F, -77.56618F, Collections.emptyList()), 1F)
    );
}
