package com.github.entwicklungsprojekt.shop.service;

import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapConnectionService;
import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapLocationService;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import com.github.entwicklungsprojekt.shop.projection.ShopProjection;
import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;

    private final HibernateSearchService shopSearchService;

    private final OpenstreetmapConnectionService openstreetmapConnectionService;

    private final OpenstreetmapLocationService openstreetmapLocationService;

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

    public Shop addShop(String shopName, String shopType, String shopLocation) {
        Shop shop = new Shop(shopName, shopType, shopLocation);
        shopRepository.save(shop);
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(shop);

        return shop;
    }

    public Shop editShop(Long shopId, String newName, String newType, String newLocation) {
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
