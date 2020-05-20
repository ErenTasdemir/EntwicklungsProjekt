package com.github.entwicklungsprojekt.radius_search.rest;

import com.github.entwicklungsprojekt.radius_search.service.RadiusSearchService;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.projection.ShopProjectionWithoutPicture;
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

    private final RadiusSearchService radiusSearchService;

    private final ProjectionFactory projectionFactory;

    public RadiusSearchController(ShopService shopService, RadiusSearchService radiusSearchService, ProjectionFactory projectionFactory) {
        this.shopService = shopService;
        this.radiusSearchService = radiusSearchService;
        this.projectionFactory = projectionFactory;
    }

    @GetMapping(path = "/searchbyradius", params = {"location" , "radius", "query"})
    public ResponseEntity<?> searchByRadius(@RequestParam(name = "location") String location,
                                            @RequestParam(name = "radius") int radius,
                                            @RequestParam(name = "query") String query) {
        var matchingShopLocations = shopService.searchShops(query);
        if (location.isEmpty()) {
            return ResponseEntity.ok(getShopProjectionsFromList(matchingShopLocations));
        }
        else {
            var shopIdsInRadius = radiusSearchService.getShopIdsWithinRadius(location, radius);
            var shopsToReturn = getShopProjectionsForMatchingShopIds(matchingShopLocations, shopIdsInRadius);

            return ResponseEntity.ok(shopsToReturn);
        }
    }

    private List<ShopProjectionWithoutPicture> getShopProjectionsFromList(List<Shop> shopList) {
        List<ShopProjectionWithoutPicture> projections = new ArrayList<>();
        shopList.forEach(shop -> projections.add(projectionFactory.createProjection(ShopProjectionWithoutPicture.class, shop)));
        return projections;
    }

    private List<ShopProjectionWithoutPicture> getShopProjectionsForMatchingShopIds(List<Shop> shopsToMatchIn, List<Long> idsToSearchFor) {
        List<ShopProjectionWithoutPicture> projections = new ArrayList<>();
        idsToSearchFor.forEach(shopId -> {
            for (Shop shop: shopsToMatchIn ) {
                if (shop.getShopId().equals(shopId)) {
                    projections.add(projectionFactory.createProjection(ShopProjectionWithoutPicture.class, shop));
                }
            }
        });
        return projections;
    }


}
