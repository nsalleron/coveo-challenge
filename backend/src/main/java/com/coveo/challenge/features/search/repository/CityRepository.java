package com.coveo.challenge.features.search.repository;

import com.coveo.challenge.features.search.service.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer>, TsvInitializer {
    List<CityEntity> findByNameContaining(String name);
}
