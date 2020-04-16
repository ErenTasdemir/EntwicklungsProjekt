package com.github.entwicklungsprojekt.shop.service;

import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import com.github.entwicklungsprojekt.shop.projection.ShopProjection;
import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import lombok.AllArgsConstructor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;

    private final HibernateSearchService shopSearchService;

    private final ProjectionFactory projectionFactory;

    public List<ShopProjection> getAllAvailibleShops() {
        return shopRepository.findAllProjectedBy();
    }

    public List<ShopProjection> searchShops(String query) {
        var shops = shopSearchService.searchShops(query);
        List<ShopProjection> projections = new ArrayList<>();

        shopSearchService.searchShops(query).forEach(shop -> projections.add(projectionFactory.createProjection(ShopProjection.class, shop)));

        return projections;
    }

    public Shop getShopById(Long id) {
        return shopRepository.getOne(id);
    }


}
