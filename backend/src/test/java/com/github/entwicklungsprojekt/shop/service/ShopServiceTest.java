package com.github.entwicklungsprojekt.shop.service;

import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapConnectionService;
import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapLocationService;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import com.github.entwicklungsprojekt.shop.projection.ShopProjection;
import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import com.github.entwicklungsprojekt.user.persistence.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ShopServiceTest {

    private final ShopProjection shopProjectionMock = Mockito.mock(ShopProjection.class);

    private final Shop shopMock = Mockito.mock(Shop.class);

    private final User userMock = Mockito.mock(User.class);

    @Mock
    private ShopRepository shopRepositoryMock;

    @Mock
    private HibernateSearchService shopSearchServiceMock;

    @Mock
    private OpenstreetmapConnectionService openstreetmapConnectionServiceMock;

    @Mock
    private OpenstreetmapLocationService openstreetmapLocationServiceMock;

    @InjectMocks
    private ShopService shopService;

    @Test
    public void getAllAvailibleShopsShouldReturnAllShops() {
        //given
        List<ShopProjection> expectedList = new ArrayList<>();
        expectedList.add(shopProjectionMock);
        given(shopRepositoryMock.findAllProjectedBy()).willReturn(expectedList);

        //when
        List<ShopProjection> actualList = shopService.getAllAvailibleShops();

        //then
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    public void searchShopsShouldReturnAllShopsIfQueryIsEmpty() {
        //given
        String query = "";
        List<Shop> expectedShops = new ArrayList<>();
        expectedShops.add(shopMock);
        given(shopRepositoryMock.findAll()).willReturn(expectedShops);

        //when
        List<Shop> actualShops = shopService.searchShops(query);

        //then
        assertThat(actualShops).isEqualTo(expectedShops);
    }

    @Test
    public void searchShopsShouldReturnFilteredShopsForGivenQuery() {
        //given
        String query = "query";
        List<Shop> expectedShops = new ArrayList<>();
        expectedShops.add(shopMock);
        given(shopSearchServiceMock.searchShops(query)).willReturn(expectedShops);

        //when
        List<Shop> actualShops = shopService.searchShops(query);

        //then
        assertThat(actualShops).isEqualTo(expectedShops);
    }

    @Test
    public void getShopByIdShouldReturnShopByGivenId() {
        //given
        Long id = 1L;
        given(shopRepositoryMock.getOne(id)).willReturn(shopMock);

        //when
        Shop actualShop = shopService.getShopById(id);

        //then
        assertThat(actualShop).isEqualTo(shopMock);
    }

    @Test
    public void addShopShouldReturnAddedShop() {
        //given
        String expectedShopName = "name";
        String expectedShopType = "type";
        String expectedShopLocation = "location";
        given(openstreetmapLocationServiceMock.isRealLocation(expectedShopLocation)).willReturn(true);

        //when
        Shop actualShop = shopService.addShop(expectedShopName, expectedShopLocation, expectedShopType, userMock);

        //then
        verify(shopRepositoryMock).save(Mockito.any(Shop.class));
        verify(openstreetmapConnectionServiceMock).setLatitudeAndLongitudeForGivenShop(Mockito.any(Shop.class));
        assertThat(actualShop.getShopName()).isEqualTo(expectedShopName);
        assertThat(actualShop.getShopType()).isEqualTo(expectedShopType);
        assertThat(actualShop.getShopLocation()).isEqualTo(expectedShopLocation);
    }

    @Test
    public void editShopShouldReturnEditedShop() {
        //given
        String expectedShopName = "name";
        String expectedShopType = "type";
        String expectedShopLocation = "location";
        Long expectedId = 1L;
        Shop expectedShop = new Shop();
        expectedShop.setShopName(expectedShopName);
        expectedShop.setShopLocation(expectedShopLocation);
        expectedShop.setShopType(expectedShopType);
        expectedShop.setUser(userMock);

        given(shopRepositoryMock.findById(expectedId)).willReturn(Optional.of(expectedShop));

        //when
        Shop actualShop = shopService.editShop(expectedId, expectedShopName, expectedShopLocation, expectedShopType, userMock);

        //then
        verify(shopRepositoryMock).save(expectedShop);
        verify(openstreetmapConnectionServiceMock).setLatitudeAndLongitudeForGivenShop(expectedShop);
        assertThat(actualShop.getShopName()).isEqualTo(expectedShopName);
        assertThat(actualShop.getShopType()).isEqualTo(expectedShopType);
        assertThat(actualShop.getShopLocation()).isEqualTo(expectedShopLocation);
    }

    @Test
    public void deleteShopShouldReturnDeletedShop() {
        //given
        //given
        String expectedShopName = "name";
        String expectedShopType = "type";
        String expectedShopLocation = "location";
        Long expectedId = 1L;
        Shop expectedShop = new Shop();
        expectedShop.setShopName(expectedShopName);
        expectedShop.setShopLocation(expectedShopLocation);
        expectedShop.setShopType(expectedShopType);
        expectedShop.setUser(userMock);

        given(shopRepositoryMock.findById(expectedId)).willReturn(Optional.of(expectedShop));


        //when
        Shop actualShop = shopService.deleteShop(expectedId, userMock);

        //then
        assertThat(actualShop).isEqualTo(expectedShop);
    }

}
