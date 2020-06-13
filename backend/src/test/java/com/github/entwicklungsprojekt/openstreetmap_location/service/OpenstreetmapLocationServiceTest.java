package com.github.entwicklungsprojekt.openstreetmap_location.service;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.GeoData;
import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import com.github.entwicklungsprojekt.openstreetmap_location.repository.OpenstreetmapLocationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OpenstreetmapLocationServiceTest {

    @Mock
    private OpenstreetmapLocationRepository openstreetmapLocationRepositoryMock;

    private String defaultCsvPath = "src/test/resources/testStaedte.csv";

    private OpenstreetmapLocationService openstreetmapLocationService;

    @Before
    public void setup() throws IOException {
        this.openstreetmapLocationService = new OpenstreetmapLocationService(defaultCsvPath, openstreetmapLocationRepositoryMock);
    }

    @Test
    public void getOneByLocationName() {
        //given
        String locationName = "location";
        OpenstreetmapLocation expectedOpenStreetMapLocation = new OpenstreetmapLocation();
        expectedOpenStreetMapLocation.setId(1L);
        expectedOpenStreetMapLocation.setName(locationName);
        expectedOpenStreetMapLocation.setGeoData(new GeoData(0.0, 0.0));

        when(openstreetmapLocationRepositoryMock.findByName(locationName)).thenReturn(expectedOpenStreetMapLocation);

        //then
        assertThat(openstreetmapLocationService.getOneByLocationName(locationName)).isEqualTo(expectedOpenStreetMapLocation);
    }

    @Test
    public void existsByName() {
        //given
        String locationName = "location";

        when(openstreetmapLocationRepositoryMock.existsByName(locationName)).thenReturn(true);

        //then
        assertThat(openstreetmapLocationService.existsByName(locationName)).isTrue();

    }


    @Test
    public void matchShopLocationWithOsmLocationCsv() {
        //given
        String location = "bad homburg und frankfurt am main";

        List<String> matchedLocations = openstreetmapLocationService.matchShopLocationWithOsmLocationCsv(location);

        assertThat(matchedLocations).containsExactlyInAnyOrder("bad homburg" , "frankfurt am main");
    }

}
