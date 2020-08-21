package com.github.entwicklungsprojekt.radius_search.service;

import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapConnectionService;
import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapLocationService;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RadiusSearchServiceTest {

    private final Shop shopMock = Mockito.mock(Shop.class);

    @Mock
    private OpenstreetmapConnectionService openstreetmapConnectionServiceMock;

    @Mock
    private ShopRepository shopRepositoryMock;

    @Mock
    private OpenstreetmapLocationService openstreetmapLocationServiceMock;

    @InjectMocks
    private RadiusSearchService radiusSearchService;

    @Test
    public void getShopIdsWithinRadiusReturnsAllByShopLocationContainsWhenRadiusIsZero() {
        //given
        Long expectedShopId = 1L;

        List<Long> expectedShopIds = new ArrayList<>();
        expectedShopIds.add(expectedShopId);

        String expectedLocation = "location";
        Shop expectedShop = shopMock;
        expectedShop.setShopLocation(expectedLocation);

        List<Shop> expectedShops = new ArrayList<>();
        expectedShops.add(shopMock);

        given(shopRepositoryMock.findAllByShopLocationIgnoreCaseContaining(expectedLocation)).willReturn(expectedShops);
        given(expectedShop.getShopId()).willReturn(expectedShopId);

        //when
        List<Long> actualShopIds = radiusSearchService.getShopIdsWithinRadius(expectedLocation, 0);

        //then
        assertThat(actualShopIds).isEqualTo(expectedShopIds);
    }

}
