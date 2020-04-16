package com.github.entwicklungsprojekt.shop.rest;

import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import com.github.entwicklungsprojekt.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopService;

    private final ProjectionFactory projectionFactory;

    @Autowired
    public ShopController(ShopService shopService, @Qualifier("shopSearchService") HibernateSearchService shopSearchService, ProjectionFactory projectionFactory) {
        this.shopService = shopService;
        this.projectionFactory = projectionFactory;
    }

    @GetMapping
    ResponseEntity<?> getAllShops() {
        return ResponseEntity.ok(shopService.getAllAvailibleShops());
    }

    @GetMapping(path = "/search")
    ResponseEntity<?> searchShops(@RequestParam(name = "query") String query) {
        return ResponseEntity.ok(shopService.searchShops(query));
    }



}
