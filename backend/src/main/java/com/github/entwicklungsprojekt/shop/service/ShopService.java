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

import java.util.List;

/**
 * A Service Class containing methods to manage {@link Shop}s.
 */
@AllArgsConstructor
@Service
@Slf4j
public class ShopService {

    private final ShopRepository shopRepository;
    private final HibernateSearchService shopSearchService;
    private final OpenstreetmapConnectionService openstreetmapConnectionService;
    private final OpenstreetmapLocationService openstreetmapLocationService;


    /**
     * Gets all availible shops.
     *
     * @return the all availible shops
     */
    public List<ShopProjection> getAllAvailibleShops() {
        return shopRepository.findAllProjectedBy();
    }

    /**
     * Search shops list.
     *
     * @param query the query
     * @return the list
     */
    public List<Shop> searchShops(String query) {
        if (query.isEmpty()) {
            return shopRepository.findAll();
        }
        return shopSearchService.searchShops(query);
    }

    /**
     * Gets {@link Shop} by id.
     *
     * @param id the id
     * @return the {@link Shop} by id
     */
    public Shop getShopById(Long id) {
        return shopRepository.getOne(id);
    }

    /**
     * Add {@link Shop}.
     *
     * @param shopName     the {@link Shop} name
     * @param shopLocation the {@link Shop} location
     * @param shopType     the {@link Shop} type
     * @param user         the {@link User}
     * @return the {@link Shop}
     */
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

    /**
     * Edit {@link Shop}.
     *
     * @param shopId      the {@link Shop} id
     * @param newName     the new name
     * @param newLocation the new location
     * @param newType     the new type
     * @param user        the {@link User}
     * @return the {@link Shop}
     */
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

    /**
     * Delete {@link Shop}.
     *
     * @param shopId the {@link Shop} id
     * @param user   the {@link User}
     * @return the {@link Shop}
     */
    public Shop deleteShop(Long shopId, User user) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(ShopNotFoundException::new);
        if (!shop.getUser().equals(user)){
            throw new UserNotAuthorizedForActionException();
        }
        shopRepository.delete(shop);
        return shop;
    }

    /**
     * Gets all {@link Shop}s of user.
     *
     * @param user the {@link User}
     * @return all {@link Shop}s of user
     */
    public List<Shop> getAllShopsOfUser(User user) {
        return shopRepository.findAllByUser(user);
    }


}
