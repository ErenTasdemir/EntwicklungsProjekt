package com.github.entwicklungsprojekt.radius_search.service;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.GeoData;
import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapConnectionService;
import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapLocationService;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RadiusSearchService {
    private final OpenstreetmapConnectionService openstreetmapConnectionService;
    private final ShopRepository shopRepository;
    private final OpenstreetmapLocationService openstreetmaplocationservice;

    public RadiusSearchService(OpenstreetmapConnectionService openstreetmapConnectionService, ShopRepository shopRepository, OpenstreetmapLocationService openstreetmaplocationservice) {
        this.openstreetmapConnectionService = openstreetmapConnectionService;
        this.shopRepository = shopRepository;
        this.openstreetmaplocationservice = openstreetmaplocationservice;
    }

    public List<Long> getShopIdsWithinRadius(String location , int radius) {
        List<Long> allProjectIdsInRadius;

        if (radius == 0) {
            allProjectIdsInRadius = new ArrayList<>();
            shopRepository.findAllByShopLocationContains(location).forEach(shop -> allProjectIdsInRadius.add(shop.getShopId()));
        } else {
            GeoData geoData = new GeoData();
            if (openstreetmaplocationservice.existsByName(location)) {
                log.info("Retrieving Geodata for " + location + " from Database");
                OpenstreetmapLocation openStreetMapLocation = openstreetmaplocationservice.getOneByLocationName(location);
                geoData.setLatitude(openStreetMapLocation.getLatitude());
                geoData.setLongitude(openStreetMapLocation.getLongitude());
            }
            else {
                geoData = openstreetmapConnectionService.getLatitudeAndLongitudeFromNominatim(location);
                OpenstreetmapLocation openStreetMapLocation = new OpenstreetmapLocation();
                openStreetMapLocation.setLatitude(geoData.getLatitude());
                openStreetMapLocation.setLongitude(geoData.getLongitude());
                openStreetMapLocation.setName(location);
                openstreetmaplocationservice.saveLocation(openStreetMapLocation);
            }
            allProjectIdsInRadius = openstreetmaplocationservice.getShopIdsInRadius(geoData , radius);
        }

        return allProjectIdsInRadius;
    }

}
