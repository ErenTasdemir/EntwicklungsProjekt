package com.github.entwicklungsprojekt.shop.rest;

import com.github.entwicklungsprojekt.shop.payload.ShopPayload;
import com.github.entwicklungsprojekt.shop.projection.ShopProjectionWithPicture;
import com.github.entwicklungsprojekt.shop.projection.ShopProjectionWithoutPicture;
import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import com.github.entwicklungsprojekt.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/shops")
@CrossOrigin(origins = "http://localhost:4200")
public class ShopController {

    private final ShopService shopService;
    private final ProjectionFactory projectionFactory;
    private final HibernateSearchService shopSearchService;

    @Autowired
    public ShopController(ShopService shopService, @Qualifier("shopSearchService") HibernateSearchService shopSearchService, ProjectionFactory projectionFactory) {
        this.shopService = shopService;
        this.projectionFactory = projectionFactory;
        this.shopSearchService = shopSearchService;
    }

    @GetMapping
    ResponseEntity<?> getAllShopsWithoutPictures() {
        var shopsWithoutPictures = new ArrayList<>();
        var shops = shopService.getAllAvailibleShops();
        shops.forEach(shop -> {
            if (shop.getShopImage()!=null) {
                shop.setShopImage(ShopService.decompressBytes(shop.getShopImage()));
            }
            shopsWithoutPictures.add(projectionFactory.createProjection(ShopProjectionWithPicture.class, shop));
        });
        return ResponseEntity.ok(shopsWithoutPictures);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getOneShop(@PathVariable String id) {
        var shop = shopService.getShopWithPictureById(Long.parseLong(id));

        return ResponseEntity.ok(projectionFactory.createProjection(ShopProjectionWithPicture.class, shop));
    }

    @GetMapping(path = "/search")
    ResponseEntity<?> searchShops(@RequestParam(name = "query") String query) {
        if (query.isEmpty()) {
            return ResponseEntity.ok(shopService.getAllAvailibleShops());
        }
        List<ShopProjectionWithoutPicture> projections = new ArrayList<>();
        shopSearchService.searchShops(query).forEach(shop -> projections.add(projectionFactory.createProjection(ShopProjectionWithoutPicture.class, shop)));

        return ResponseEntity.ok(projections);
    }

    @PostMapping(path = "/add")
    ResponseEntity<?> addShop(@RequestBody ShopPayload shopPayload) {
        var shop = shopService.addShop(shopPayload.getShopName(), shopPayload.getShopLocation(), shopPayload.getShopType());
        var projection = projectionFactory.createProjection(ShopProjectionWithoutPicture.class, shop);

        return ResponseEntity.ok(projection);
    }

    @PostMapping(path = "/{id}/edit")
    ResponseEntity<?> editShop(@PathVariable(name = "id")String id, @RequestBody ShopPayload shopPayload) {
        var shop = shopService.editShop(Long.parseLong(id), shopPayload.getShopName(), shopPayload.getShopType(), shopPayload.getShopLocation());
        var projection = projectionFactory.createProjection(ShopProjectionWithoutPicture.class, shop);

        return ResponseEntity.ok(projection);
    }

    @Transactional
    @DeleteMapping(path = "/{id}/delete")
    ResponseEntity<?> deleteShop(@PathVariable(name = "id")String id) {
        var shop = shopService.deleteShop(Long.parseLong(id));
        var projection = projectionFactory.createProjection(ShopProjectionWithoutPicture.class, shop);

        return ResponseEntity.ok(projection);
    }

    @PostMapping("/{id}/upload")
    ResponseEntity<?> uploadImage(@PathVariable(name = "id") String id, @RequestParam(name = "imageFile")MultipartFile file) throws IOException {
        return ResponseEntity.ok(projectionFactory.createProjection(ShopProjectionWithPicture.class, shopService.saveImageToShop(Long.parseLong(id), file)));
    }

}
