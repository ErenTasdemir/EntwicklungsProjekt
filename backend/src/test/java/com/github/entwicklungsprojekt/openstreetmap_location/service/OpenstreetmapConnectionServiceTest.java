package com.github.entwicklungsprojekt.openstreetmap_location.service;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.GeoData;
import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenstreetmapConnectionServiceTest {

    @Spy
    private final Shop shopMock = Mockito.mock(Shop.class);

    private final OpenstreetmapLocation openstreetmapLocationMock = Mockito.mock(OpenstreetmapLocation.class);

    private final GeoData geoDataMock = Mockito.mock(GeoData.class);


    @Mock
    private OpenstreetmapLocationService openstreetmapLocationServiceMock;

    @Mock
    private ShopRepository shopRepositoryMock;


    @InjectMocks
    @Spy
    private OpenstreetmapConnectionService openstreetmapConnectionService;

    @Test
    public void setLatitudeAndLongitudeForLoadedShopsSetsGeodataForAllLoadedShops() {
        //given
        List<Shop> expectedShopList = new ArrayList<>();
        expectedShopList.add(shopMock);
        expectedShopList.add(shopMock);

        given(shopRepositoryMock.findAll()).willReturn(expectedShopList);

        //when
        openstreetmapConnectionService.setLatitudeAndLongitueForLoadedShops();

        //then
        verify(openstreetmapConnectionService, times(2)).setLatitudeAndLongitudeForGivenShop(shopMock);

    }

    @Test
    public void setLatitudeAndLongitudeForGivenShopSavesNewLocationToShopAndMakesNominatimRequestIfLocationDoesntExist() {
        //given
        Set<OpenstreetmapLocation> expectedOpenstreetmapLocationSet = Set.of(openstreetmapLocationMock);
        String expectedLocation = "location";
        List<String> expectedMatchedLocations = new ArrayList<>();
        expectedMatchedLocations.add(expectedLocation);

        Shop expectedShop = shopMock;
        expectedShop.setLocations(expectedOpenstreetmapLocationSet);

        given(openstreetmapLocationServiceMock.matchShopLocationWithOsmLocationCsv(expectedShop.getShopLocation())).willReturn(expectedMatchedLocations);
        given(openstreetmapLocationServiceMock.existsByName(expectedLocation)).willReturn(false);
        given(openstreetmapConnectionService.getLatitudeAndLongitudeFromNominatim(expectedLocation)).willReturn(geoDataMock);

        //when
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(expectedShop);

        //then
        verify(openstreetmapLocationServiceMock).saveLocation(Mockito.any(OpenstreetmapLocation.class));

    }

    @Test
    public void setLatitudeAndLongitudeForGivenShopSavesNewLocationToShopAndMakesNoNominatimRequestIfLocationAlreadyExists() {
        //given
        Set<OpenstreetmapLocation> expectedOpenstreetmapLocationSet = Set.of(openstreetmapLocationMock);
        String expectedLocation = "location";
        List<String> expectedMatchedLocations = new ArrayList<>();
        expectedMatchedLocations.add(expectedLocation);

        Shop expectedShop = shopMock;
        expectedShop.setLocations(expectedOpenstreetmapLocationSet);

        given(openstreetmapLocationServiceMock.matchShopLocationWithOsmLocationCsv(expectedShop.getShopLocation())).willReturn(expectedMatchedLocations);
        given(openstreetmapLocationServiceMock.existsByName(expectedLocation)).willReturn(true);
        given(openstreetmapLocationServiceMock.getOneByLocationName(expectedLocation)).willReturn(openstreetmapLocationMock);

        //when
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(expectedShop);

        //then
        verify(openstreetmapLocationServiceMock).saveLocation(Mockito.any(OpenstreetmapLocation.class));
    }

    @Test
    public void setLatitudeAndLongitudeForGivenShopDoesntSaveNewLocationIfShopLocationsIsEmpty() {
        //given
        given(shopMock.getLocations()).willReturn(null);

        //when
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(shopMock);
        verify(openstreetmapLocationServiceMock, times(0)).saveLocation(openstreetmapLocationMock);
    }



}
