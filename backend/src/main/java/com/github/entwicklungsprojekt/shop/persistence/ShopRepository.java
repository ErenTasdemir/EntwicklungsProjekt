package com.github.entwicklungsprojekt.shop.persistence;

import com.github.entwicklungsprojekt.shop.projection.ShopProjection;
import com.github.entwicklungsprojekt.user.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link JpaRepository} for persisting {@link Shop}s.
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<ShopProjection> findAllProjectedBy();

    List<Shop> findAllByShopLocationContains(String location);

    List<Shop> findAllByUser(User user);

}
