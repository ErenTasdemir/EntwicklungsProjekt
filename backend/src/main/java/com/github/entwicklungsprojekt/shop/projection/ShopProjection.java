package com.github.entwicklungsprojekt.shop.projection;

import com.github.entwicklungsprojekt.shop.persistence.Shop;

/**
 * Projection of {@link Shop}s only containing data that is relevant to the client.
 */
public interface ShopProjection {

    Long getShopId();

    String getShopName();

    String getShopType();

    String getShopLocation();
}
