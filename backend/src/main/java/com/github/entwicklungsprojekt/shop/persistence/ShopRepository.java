package com.github.entwicklungsprojekt.shop.persistence;

import com.github.entwicklungsprojekt.shop.projection.ShopProjectionWithPicture;
import com.github.entwicklungsprojekt.shop.projection.ShopProjectionWithoutPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<ShopProjectionWithPicture> findAllProjectedBy();

    List<Shop> findAllByShopLocationContains(String location);

}
