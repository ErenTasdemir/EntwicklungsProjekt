package com.github.entwicklungsprojekt.shop.service;

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


}
