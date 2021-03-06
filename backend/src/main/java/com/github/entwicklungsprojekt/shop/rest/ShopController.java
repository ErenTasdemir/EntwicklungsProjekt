package com.github.entwicklungsprojekt.shop.rest;

import com.github.entwicklungsprojekt.shop.payload.ShopPayload;
import com.github.entwicklungsprojekt.shop.projection.ShopProjection;
import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import com.github.entwicklungsprojekt.shop.service.ShopService;
import com.github.entwicklungsprojekt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/shops")
@CrossOrigin(origins = "http://localhost:4200")
public class ShopController {

    private final ShopService shopService;
    private final ProjectionFactory projectionFactory;
    private final HibernateSearchService shopSearchService;
    private final UserService userService;

    @Autowired
    public ShopController(ShopService shopService, @Qualifier("shopSearchService") HibernateSearchService shopSearchService, ProjectionFactory projectionFactory, UserService userService) {
        this.shopService = shopService;
        this.projectionFactory = projectionFactory;
        this.shopSearchService = shopSearchService;
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<?> getAllShops() {
        return ResponseEntity.ok(shopService.getAllAvailibleShops());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getOneShop(@PathVariable String id) {
        var shop = shopService.getShopById(Long.parseLong(id));

        return ResponseEntity.ok(projectionFactory.createProjection(ShopProjection.class, shop));
    }

    @GetMapping(path = "/search")
    ResponseEntity<?> searchShops(@RequestParam(name = "query") String query) {
        if (query.isEmpty()) {
            return ResponseEntity.ok(shopService.getAllAvailibleShops());
        }
        List<ShopProjection> projections = new ArrayList<>();
        shopSearchService.searchShops(query).forEach(shop -> projections.add(projectionFactory.createProjection(ShopProjection.class, shop)));

        return ResponseEntity.ok(projections);
    }

    @PostMapping(path = "/add")
    ResponseEntity<?> addShop(@RequestBody ShopPayload shopPayload, Principal principal) {
        var user = userService.findByUsername(principal.getName());
        var shop = shopService.addShop(shopPayload.getShopName(), shopPayload.getShopLocation(), shopPayload.getShopType(), user);
        var projection = projectionFactory.createProjection(ShopProjection.class, shop);

        return ResponseEntity.ok(projection);
    }

    @PostMapping(path = "/{id}/edit")
    ResponseEntity<?> editShop(@PathVariable(name = "id")String id, @RequestBody ShopPayload shopPayload, Principal principal) {
        var user = userService.findByUsername(principal.getName());
        var shop = shopService.editShop(Long.parseLong(id), shopPayload.getShopName(), shopPayload.getShopLocation(), shopPayload.getShopType(), user);
        var projection = projectionFactory.createProjection(ShopProjection.class, shop);

        return ResponseEntity.ok(projection);
    }

    @Transactional
    @DeleteMapping(path = "/{id}/delete")
    ResponseEntity<?> deleteShop(@PathVariable(name = "id")String id, Principal principal) {
        var user = userService.findByUsername(principal.getName());
        var shop = shopService.deleteShop(Long.parseLong(id), user);
        var projection = projectionFactory.createProjection(ShopProjection.class, shop);

        return ResponseEntity.ok(projection);
    }

    @GetMapping(path = "/my")
    ResponseEntity<?> getShopsForUser(Principal principal) {
        var user = userService.findByUsername(principal.getName());
        var shops = shopService.getAllShopsOfUser(user);

        List<ShopProjection> projections = new ArrayList<>();
        shops.forEach(shop -> projections.add(projectionFactory.createProjection(ShopProjection.class, shop)));

        return ResponseEntity.ok(projections);
    }

}
