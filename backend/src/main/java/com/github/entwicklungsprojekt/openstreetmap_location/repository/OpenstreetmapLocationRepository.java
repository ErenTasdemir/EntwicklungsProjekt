package com.github.entwicklungsprojekt.openstreetmap_location.repository;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenstreetmapLocationRepository extends JpaRepository<OpenstreetmapLocation, Long> {

    boolean existsByName(String locationName);

    OpenstreetmapLocation findByName(String locationName);

}
