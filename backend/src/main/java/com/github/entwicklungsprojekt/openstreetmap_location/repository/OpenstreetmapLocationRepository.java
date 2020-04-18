package com.github.entwicklungsprojekt.openstreetmap_location.repository;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenstreetmapLocationRepository extends JpaRepository<OpenstreetmapLocation, Long> {

    boolean existsByName(String locationName);

    OpenstreetmapLocation findByName(String locationName);

    @Query(value = "SELECT shop_id " +
            "FROM " +
            "(SELECT *, ( 6371 * acos ( cos(radians( :latitude))*cos(radians( latitude))*cos(radians(longitude) - radians( :longitude))+ sin(radians( :latitude))*sin(radians (latitude)))) " +
            "AS distance " +
            "FROM openstreetmap_location " +
            "INNER JOIN shop_location " +
            "ON shop_location.location_id = openstreetmap_location.id " +
            "GROUP BY shop_location.shop_id, openstreetmap_location.id " +
            "HAVING distance <= :radius " +
            "ORDER BY distance ASC) " +
            "AS results_with_distance;" , nativeQuery = true)
    List<Long> findAllByRadius(@Param("latitude") Double latitude , @Param("longitude") Double lng , @Param("radius") int radius);
}
