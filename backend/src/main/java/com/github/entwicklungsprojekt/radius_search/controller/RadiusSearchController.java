package com.github.entwicklungsprojekt.radius_search.controller;

import com.github.entwicklungsprojekt.radius_search.service.RadiusSearchService;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.projection.ShopProjection;
import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import com.github.entwicklungsprojekt.shop.service.ShopService;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/shops/search")
public class RadiusSearchController {

    private final ShopService shopService;

    private final HibernateSearchService shopSearchService;

    private final RadiusSearchService radiusSearchService;

    private final ProjectionFactory projectionFactory;

    public RadiusSearchController(ShopService shopService, HibernateSearchService shopSearchService, RadiusSearchService radiusSearchService, ProjectionFactory projectionFactory) {
        this.shopService = shopService;
        this.shopSearchService = shopSearchService;
        this.radiusSearchService = radiusSearchService;
        this.projectionFactory = projectionFactory;
    }

    @GetMapping(path = "/searchbyradius", params = {"location" , "radius", "query"})
    public ResponseEntity<?> searchByRadius(@RequestParam(required = false) String location , @RequestParam(required = false, defaultValue = "0") int radius, @RequestParam(required = false) String query) {
        var matchingShopLocations = shopService.searchShops(query);
        if (location.equals("")) {
            return ResponseEntity.ok(getShopProjectionsFromList(matchingShopLocations));
        }
        else {
            var shopIdsInRadius = radiusSearchService.getShopIdsWithinRadius(location, radius);
            var shopsToReturn = getShopProjectionsForMatchingShopIds(matchingShopLocations, shopIdsInRadius);

            return ResponseEntity.ok(shopsToReturn);
        }
    }

    private List<ShopProjection> getShopProjectionsFromList(List<Shop> shopList) {
        List<ShopProjection> projections = new ArrayList<>();
        shopList.forEach(shop -> projections.add(projectionFactory.createProjection(ShopProjection.class, shop)));
        return projections;
    }

    private List<ShopProjection> getShopProjectionsForMatchingShopIds(List<Shop> shopsToMatchIn, List<Long> idsToSearchFor) {
        List<ShopProjection> projections = new ArrayList<>();
        idsToSearchFor.forEach(shopId -> {
            for (Shop shop: shopsToMatchIn ) {
                if (shop.getShopId().equals(shopId)) {
                    projections.add(projectionFactory.createProjection(ShopProjection.class, shop));
                }
            }
        });
        return projections;
    }


}
