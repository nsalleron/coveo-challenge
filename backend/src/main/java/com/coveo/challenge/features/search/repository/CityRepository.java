package com.coveo.challenge.features.search.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Parse CSV files
 */
@Repository
public class CityRepository {
    List<CityRecord> cities;

    final Logger logger = LoggerFactory.getLogger(CityRepository.class);

    public CityRepository(ResourceLoader resourceLoader) throws IOException {
        this.cities = new ArrayList<>(this.readCities(resourceLoader.getResource("classpath:data/cities_canada-usa.tsv").getInputStream()).values());

    }

    //We can use Spring Batch I think depending on the use case. Because OK we read data, but we didn't transform data.
    private HashMap<Number, CityRecord> readCities(InputStream file) {
        HashMap<Number, CityRecord> cities = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file))) { //Better way, avoid to forget close - called try with ressources block

            // Skip the first line.
            reader.readLine();

            // Start reading fields.
            String line;
            while ((line = reader.readLine()) != null) { //I find it weird otherwise
                // Because it's a tab separated file, split all the lines on the tab char.
                String[] fields = line.split("\t");

                String[] altNames = fields[3].split(",");
                if (altNames.length == 1 && altNames[0].isEmpty()) {
                    altNames = new String[0];
                }
                // Populate the city object with the fields.
                CityRecord city = new CityRecord(Integer.parseInt(fields[0]), fields[1], fields[17].replace("_", " "), altNames, Float.parseFloat(fields[4]), Float.parseFloat(fields[5]));

                // Add the city to the return value.
                if (cities.get(city.id()) == null) {
                    cities.put(city.id(), city);
                } else {
                    logger.warn("Duplicate city key: {}", city.id());
                }
            }
        } catch (Throwable e) {
            logger.trace("error while reading city file", e);
        }

        return cities;
    }

    public List<CityRecord> getCities() {
        return cities;
    }

}
