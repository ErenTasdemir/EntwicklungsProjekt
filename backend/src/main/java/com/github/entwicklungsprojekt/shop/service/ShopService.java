package com.github.entwicklungsprojekt.shop.service;

import com.github.entwicklungsprojekt.exceptions.NotARealLocationException;
import com.github.entwicklungsprojekt.exceptions.ShopNotFoundException;
import com.github.entwicklungsprojekt.exceptions.UserNotAuthorizedForActionException;
import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapConnectionService;
import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapLocationService;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import com.github.entwicklungsprojekt.shop.projection.ShopProjection;
import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import com.github.entwicklungsprojekt.user.persistence.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
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

    public Shop addShop(String shopName, String shopLocation, String shopType, User user) {
        if (!openstreetmapLocationService.isRealLocation(shopLocation)) {
            throw new NotARealLocationException();
        }
        Shop shop = new Shop(shopName, shopLocation, shopType, user);
        shopRepository.save(shop);
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(shop);

        log.info("Shop with name {} has been added.", shop.getShopName());

        return shop;
    }

    public Shop editShop(Long shopId, String newName, String newLocation, String newType, User user) {
        Shop shopToEdit = shopRepository.findById(shopId).orElseThrow(ShopNotFoundException::new);
        if (!shopToEdit.getUser().equals(user)){
            throw new UserNotAuthorizedForActionException();
        }
        shopToEdit.setShopName(newName);
        shopToEdit.setShopType(newType);
        shopToEdit.setShopLocation(newLocation);
        shopRepository.save(shopToEdit);
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(shopToEdit);

        return shopToEdit;
    }

    public Shop deleteShop(Long shopId, User user) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(ShopNotFoundException::new);
        if (!shop.getUser().equals(user)){
            throw new UserNotAuthorizedForActionException();
        }
        shopRepository.delete(shop);
        return shop;
    }

    public List<Shop> getAllShopsOfUser(User user) {
        return shopRepository.findAllByUser(user);
    }


}
