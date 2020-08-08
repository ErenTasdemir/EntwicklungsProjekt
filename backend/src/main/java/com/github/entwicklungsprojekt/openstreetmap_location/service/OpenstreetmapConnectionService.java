package com.github.entwicklungsprojekt.openstreetmap_location.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.entwicklungsprojekt.openstreetmap_location.persistence.GeoData;
import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A Service Class containing methods concerning the connection to the Nominatim API
 */
@Slf4j
@Transactional
@Service
public class OpenstreetmapConnectionService {

    private final OpenstreetmapLocationService openstreetmapLocationService;

    private final ShopRepository shopRepository;

    public OpenstreetmapConnectionService(OpenstreetmapLocationService openstreetmapLocationService, ShopRepository shopRepository) {
        this.openstreetmapLocationService = openstreetmapLocationService;
        this.shopRepository = shopRepository;
    }

    /**
     * Sets Geodata for all loaded Shops
     */
    @EventListener(ApplicationReadyEvent.class)
    public void setLatitudeAndLongitueForLoadedShops() {
        log.info("Setting Geodata for loaded shops");
        for (Shop shop : shopRepository.findAll()) {
            setLatitudeAndLongitudeForGivenShop(shop);
        }
        log.info("Succesfully saved Geodata for Locations!");
    }

    /**
     * Sets Geodata for Shop
     * @param shop
     *  The {@link Shop} for which the Location is to be set.
     */
    public void setLatitudeAndLongitudeForGivenShop(Shop shop) {
        if (shop.getLocations() == null || shop.getLocations().isEmpty()){
            List<String> projectLocationNames = openstreetmapLocationService.matchShopLocationWithOsmLocationCsv(shop.getShopLocation());
            for (String location : projectLocationNames) {
                OpenstreetmapLocation openstreetmapLocation;
                if (!openstreetmapLocationService.existsByName(location)) {
                    openstreetmapLocation = new OpenstreetmapLocation();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        openstreetmapLocation.setGeoData(getLatitudeAndLongitudeFromNominatim(location));
                    }
                    catch (HttpStatusCodeException | InterruptedException e) {
                        log.error("Nominatim connection has timed out");
                    }
                    openstreetmapLocation.setName(location);
                } else {
                    openstreetmapLocation = openstreetmapLocationService.getOneByLocationName(location);
                }
                openstreetmapLocation.addShop(shop);
                openstreetmapLocationService.saveLocation(openstreetmapLocation);
            }
        }
    }

    /**
     * Sends a GET-Request to the Nominatim API and retrieves {@link GeoData}.
     * @param location
     *  The location to get {@link GeoData} for.
     *
     * @return
     * {@link GeoData} for location.
     */
    public GeoData getLatitudeAndLongitudeFromNominatim(String location) {
        log.info("Retrieving Geodata for " + location + " from Nominatim");
        ResponseEntity<String> response = makeGetLocationRequest(location);
        log.info("Nominatim server status : " + response.getStatusCode().toString());
        return getGeodataFromResponseJson(response);
    }

    private ResponseEntity<String> makeGetLocationRequest(String query) {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = buildNominatimUri(query);
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);

        try {
            return restTemplate.exchange(uri, HttpMethod.GET, requestEntity ,String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        }
    }

    private URI buildNominatimUri(String query) {
        final String uri = "https://nominatim.openstreetmap.org/";
        final String email = "eren.tasdemir@hs-bochum.de";
        final String format = "json";
        final String countrycodes = "de";
        final String limit = "1";

        return UriComponentsBuilder.fromUriString(uri)
                .queryParam("email", email)
                .queryParam("format", format)
                .queryParam("countrycodes", countrycodes)
                .queryParam("limit", limit)
                .queryParam( "q", query)
                .build().encode().toUri();
    }

    private GeoData getGeodataFromResponseJson(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootArray;
        try {
            rootArray = objectMapper.readTree(response.getBody());
        } catch (IOException e) {
            throw new IllegalStateException();
        }

        GeoData geoData = new GeoData();

        if (!rootArray.has(0)) {
            geoData.setLatitude(0.0);
            geoData.setLongitude(0.0);
        } else {
            geoData.setLatitude(rootArray.get(0).path("lat").asDouble());
            geoData.setLongitude(rootArray.get(0).path("lon").asDouble());
        }

        return geoData;
    }

}

