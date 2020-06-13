package com.github.entwicklungsprojekt.shop.service;

import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapConnectionService;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import com.github.entwicklungsprojekt.shop.projection.ShopProjection;
import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import com.github.entwicklungsprojekt.user.persistence.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class ShopService {

    private final ShopRepository shopRepository;

    private final HibernateSearchService shopSearchService;

    private final OpenstreetmapConnectionService openstreetmapConnectionService;


    public List<ShopProjection> getAllAvailibleShops() {
        return shopRepository.findAllProjectedBy();
    }

    public List<Shop> searchShops(String query) {
        if (query.isEmpty()) {
            return shopRepository.findAll();
        }
        return shopSearchService.searchShops(query);
    }

    public Shop getShopById(Long id) {
        return shopRepository.getOne(id);
    }

    public Shop addShop(String shopName, String shopLocation, String shopType, User user) {
        Shop shop = new Shop(shopName, shopLocation, shopType, user);
        shopRepository.save(shop);
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(shop);

        log.info("Shop with name {} has been added.", shop.getShopName());

        return shop;
    }

    public Shop editShop(Long shopId, String newName, String newLocation, String newType) {
        Shop shopToEdit = shopRepository.getOne(shopId);
        shopToEdit.setShopName(newName);
        shopToEdit.setShopType(newType);
        shopToEdit.setShopLocation(newLocation);
        shopRepository.save(shopToEdit);
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(shopToEdit);

        return shopToEdit;
    }

    public Shop deleteShop(Long shopId) {
        Shop shop = shopRepository.getOne(shopId);
        shopRepository.delete(shop);
        return shop;
    }


}
