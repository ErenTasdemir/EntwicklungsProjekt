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

@Slf4j
@Service
@Transactional
public class OpenstreetmapLocationService {
    public List<String> loadedLocations = new ArrayList<>();

    private final String defaultCsvPath;

    private final OpenstreetmapLocationRepository openstreetmapLocationRepository;


    public OpenstreetmapLocationService(@Value("${staedte_osm.path}")String defaultCsvPath, OpenstreetmapLocationRepository openstreetmapLocationRepository) throws IOException {
        this.defaultCsvPath = defaultCsvPath;
        this.openstreetmapLocationRepository = openstreetmapLocationRepository;
        log.info("Adding location names to Memory");
        readStaedteOsmIntoMemory();
    }

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

    public OpenstreetmapLocation getOneByLocationName(String locationName) {
        return openstreetmapLocationRepository.findByName(locationName);
    }

    public boolean existsByName(String locationName) {
        return openstreetmapLocationRepository.existsByName(locationName);
    }

    public void saveLocation(OpenstreetmapLocation openstreetmapLocation) {
        openstreetmapLocationRepository.save(openstreetmapLocation);
    }

    public List<Long> getShopIdsInRadius(GeoData geoData , int radius) {
        return openstreetmapLocationRepository.findAllByRadius(geoData.getLatitude() , geoData.getLongitude() , radius);
    }

    public List<String> matchShopLocationWithOsmLocationCsv(String shopLocation) {
        List<String> shopLocations = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        shopLocation = shopLocation.toLowerCase();
        String finalShopLocation = shopLocation;
        this.loadedLocations.forEach(location -> {
            location = location.toLowerCase();
            if (containsExactly(finalShopLocation, location)){

                if (!shopLocations.isEmpty()){
                    int index = 0;
                    boolean shouldAddLocation = false;
                    boolean replaceEntry = false;
                    int replaceIndex = 0;
                    for (String alreadySavedLocation: tempList) {

                        if (containsExactly(location, alreadySavedLocation) && location.length() != alreadySavedLocation.length()){
                            shouldAddLocation = true;
                            replaceEntry = true;
                            replaceIndex = index;
                            break;
                        }
                        if (containsExactly(alreadySavedLocation, location) && location.length() != alreadySavedLocation.length()) {
                            shouldAddLocation = false;
                            replaceEntry = false;
                            break;
                        }
                        else {
                            shouldAddLocation = true;
                        }
                        index++;
                    }
                    if (shouldAddLocation) {
                        log.info("Matching " + finalShopLocation + " with " + location);
                        if(replaceEntry) {
                            shopLocations.set(replaceIndex, location);
                        }
                        else {
                            shopLocations.add(location);
                        }
                    }
                }
                else {
                    shopLocations.add(location);
                }

                tempList.add(location);
            }
        });

        return shopLocations;
    }


    private boolean containsExactly (String source , String subItem) {
        subItem = subItem.replaceAll("\\\\" , "/");
        String pattern = "\\b"+subItem+"\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);

        return m.find();
    }

}
