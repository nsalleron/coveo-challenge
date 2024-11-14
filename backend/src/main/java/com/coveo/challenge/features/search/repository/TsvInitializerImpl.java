package com.coveo.challenge.features.search.repository;

import com.coveo.challenge.features.search.service.CityEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TsvInitializerImpl implements TsvInitializer {

    @PersistenceContext
    private final EntityManager entityManager;
    private final List<CityEntity> cities;
    final Logger logger = LoggerFactory.getLogger(TsvInitializerImpl.class);

    public TsvInitializerImpl(ResourceLoader resourceLoader, EntityManager entityManager) throws IOException {
        this.entityManager = entityManager;
        cities = new ArrayList<>(this.readCities(resourceLoader.getResource("classpath:data/cities_canada-usa.tsv").getInputStream()).values());

    }

    // DO NOT DO THAT IN PRODUCTION THIS IS DUMMY
    @Override
    @Transactional
    public void initialize() {
        for (CityEntity city : cities) {
            entityManager.merge(city);
        }
    }


    private HashMap<Number, CityEntity> readCities(InputStream file) {
        HashMap<Number, CityEntity> cities = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file))) { //Better way, avoid to forget close - called try with ressources block
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
                final Integer id = Integer.parseInt(fields[0]);
                final String name = fields[1];
                final String ascii = fields[2];
                final String country = fields[17].replace("_", " ");
                final Float latitude = Float.parseFloat(fields[4]);
                final Float longitude = Float.parseFloat(fields[5]);
                final List<String> admins = new ArrayList<>();
                admins.add(fields[10]);
                admins.add(fields[11]);
                admins.add(fields[12]);
                admins.add(fields[13]);

                // Populate the city object with the fields.
                CityEntity city = new CityEntity(
                        id,
                        name,
                        ascii,
                        country,
                        List.of(altNames),
                        latitude,
                        longitude,
                        admins.stream().distinct().toList());

                // Add the city to the return value.
                if (cities.get(city.getId()) == null) {
                    cities.put(city.getId(), city);
                } else {
                    logger.warn("Duplicate city key: {}", city.getId());
                }
            }
        } catch (Throwable e) {
            logger.trace("error while reading city file", e);
        }

        return cities;
    }


}
