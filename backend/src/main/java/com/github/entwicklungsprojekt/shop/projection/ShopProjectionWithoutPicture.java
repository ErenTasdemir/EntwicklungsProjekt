package com.github.entwicklungsprojekt.shop.projection;

import org.springframework.beans.factory.annotation.Value;

public interface ShopProjectionWithoutPicture {

    Long getShopId();

    String getShopName();

    String getShopType();

    String getShopLocation();
}
