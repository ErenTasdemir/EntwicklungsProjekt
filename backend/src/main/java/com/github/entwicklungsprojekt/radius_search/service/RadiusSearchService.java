package com.github.entwicklungsprojekt.radius_search.service;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.GeoData;
import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapConnectionService;
import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapLocationService;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A service class containing methods for a radius search functionality.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RadiusSearchService {
    private final OpenstreetmapConnectionService openstreetmapConnectionService;
    private final ShopRepository shopRepository;
    private final OpenstreetmapLocationService openstreetmaplocationservice;

    /**
     * Gets shop ids within radius.
     *
     * @param location the location
     * @param radius   the radius
     * @return the shop ids within radius
     */
    public List<Long> getShopIdsWithinRadius(String location , int radius) {
        List<Long> allShopIdsInRadius;

        if (radius == 0) {
            allShopIdsInRadius = new ArrayList<>();
            shopRepository.findAllByShopLocationIgnoreCaseContaining(location).forEach(shop -> allShopIdsInRadius.add(shop.getShopId()));
        } else {
            GeoData geoData = new GeoData();
            if (openstreetmaplocationservice.existsByName(location)) {
                log.info("Retrieving Geodata for " + location + " from Database");
                OpenstreetmapLocation openStreetMapLocation = openstreetmaplocationservice.getOneByLocationName(location);
                geoData = openStreetMapLocation.getGeoData();
            }
            else {
                geoData = openstreetmapConnectionService.getLatitudeAndLongitudeFromNominatim(location);
                OpenstreetmapLocation openStreetMapLocation = new OpenstreetmapLocation();
                openStreetMapLocation.setGeoData(geoData);
                openStreetMapLocation.setName(location);
                openstreetmaplocationservice.saveLocation(openStreetMapLocation);
            }
            allShopIdsInRadius = openstreetmaplocationservice.getShopIdsInRadius(geoData , radius);
        }

        return allShopIdsInRadius;
    }

}
