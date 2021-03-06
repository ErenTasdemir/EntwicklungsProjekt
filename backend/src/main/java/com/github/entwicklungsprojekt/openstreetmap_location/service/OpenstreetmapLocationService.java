package com.github.entwicklungsprojekt.openstreetmap_location.service;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.GeoData;
import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import com.github.entwicklungsprojekt.openstreetmap_location.repository.OpenstreetmapLocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Service Class containing methods to manage {@link OpenstreetmapLocation}s.
 */
@Slf4j
@Service
@Transactional
public class OpenstreetmapLocationService {
    private List<String> loadedLocations = new ArrayList<>();

    private final String defaultCsvPath;

    private final OpenstreetmapLocationRepository openstreetmapLocationRepository;


    /**
     * Instantiates a new Openstreetmap location service.
     *
     * @param defaultCsvPath                  the default csv path
     * @param openstreetmapLocationRepository the openstreetmap location repository
     * @throws IOException the io exception
     */
    public OpenstreetmapLocationService(@Value("${staedte_osm.path}")String defaultCsvPath, OpenstreetmapLocationRepository openstreetmapLocationRepository) throws IOException {
        this.defaultCsvPath = defaultCsvPath;
        this.openstreetmapLocationRepository = openstreetmapLocationRepository;
        log.info("Adding location names to Memory");
        readStaedteOsmIntoMemory();
    }

    /**
     * Reads a list of city names into memory.
     * @throws IOException
     */
    private void readStaedteOsmIntoMemory() throws IOException {
        String filePath = new File(defaultCsvPath).getAbsolutePath();
        Reader in = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader("location")
                .parse(in);
        for (CSVRecord record : records) {
            String location = record.get("location");
            this.loadedLocations.add(location);
        }
        log.info("Succesfully added location names to memory");
    }


    /**
     * Gets one by location name.
     *
     * @param locationName the location name
     * @return the one by location name
     */
    public OpenstreetmapLocation getOneByLocationName(String locationName) {
        return openstreetmapLocationRepository.findByName(locationName);
    }

    /**
     * Exists by name boolean.
     *
     * @param locationName the location name
     * @return the boolean
     */
    public boolean existsByName(String locationName) {
        return openstreetmapLocationRepository.existsByName(locationName);
    }

    /**
     * Save location.
     *
     * @param openstreetmapLocation the openstreetmap location
     */
    public void saveLocation(OpenstreetmapLocation openstreetmapLocation) {
        openstreetmapLocationRepository.save(openstreetmapLocation);
    }

    /**
     * Gets shop ids in radius.
     *
     * @param geoData the geo data
     * @param radius  the radius
     * @return the shop ids in radius
     */
    public List<Long> getShopIdsInRadius(GeoData geoData , int radius) {
        return openstreetmapLocationRepository.findAllByRadius(geoData.getLatitude() , geoData.getLongitude() , radius);
    }

    /**
     * Match shop location with osm location csv list.
     *
     * @param shopLocation the shop location
     * @return the list
     */
    public List<String> matchShopLocationWithOsmLocationCsv(String shopLocation) {
        List<String> matchedCityNames = new ArrayList<>();
        this.loadedLocations.forEach(cityName -> updateShopLocationsListIfMatches(shopLocation, matchedCityNames, cityName));
        return matchedCityNames;
    }

    /**
     * Is real location boolean.
     *
     * @param location the location
     * @return the boolean
     */
    public boolean isRealLocation(String location) {
        List<String> extractedLocations = matchShopLocationWithOsmLocationCsv(location);
        if (extractedLocations.isEmpty()){
            return false;
        }
        return true;
    }

    private void updateShopLocationsListIfMatches(String shopLocation, List<String> matchedCityNames, String cityName) {
        if (containsExactly(shopLocation, cityName)) {
            if (matchedCityNames.isEmpty()) {
                matchedCityNames.add(cityName.toLowerCase());
            } else {
                AddOrReplaceDecision addOrReplaceDecision = new AddOrReplaceDecision();
                addOrReplaceDecision.applyToList(matchedCityNames, cityName.toLowerCase());
            }
        }
    }

    private boolean containsExactly (String source , String subItem) {
        subItem = subItem.replaceAll("\\\\" , "/");
        String pattern = "\\b"+subItem+"\\b";
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);

        return m.find();
    }

    private class AddOrReplaceDecision {

        boolean shouldPutIntoList;
        boolean shouldReplaceAnEntry;

        private void decideAddOrReplace(String matchedCityName, String cityName) {
            if (cityName.length() == matchedCityName.length()) {
                add();
            }
            if (containsExactly(cityName, matchedCityName)) {
                replace();
            }
            else if (containsExactly(matchedCityName, cityName)) {
                dontAddOrReplace();
            }
            else {
                add();
            }
        }

        private void add() {
            this.shouldPutIntoList = true;
            this.shouldReplaceAnEntry = false;
        }

        private void replace() {
            this.shouldPutIntoList = true;
            this.shouldReplaceAnEntry = true;
        }

        private void dontAddOrReplace() {
            this.shouldPutIntoList = false;
            this.shouldReplaceAnEntry = false;
        }

        private void applyToList(List<String> matchedCityNames, String cityName) {
            int index = 0;
            for (String matchedCityName : matchedCityNames){
                decideAddOrReplace(matchedCityName, cityName);
                if (this.shouldReplaceAnEntry) {
                    break;
                }
                index+=1;
            }
            if (this.shouldReplaceAnEntry){
                matchedCityNames.set(index, cityName);
            } else if (this.shouldPutIntoList) {
                matchedCityNames.add(cityName);
            }
        }
    }

}
