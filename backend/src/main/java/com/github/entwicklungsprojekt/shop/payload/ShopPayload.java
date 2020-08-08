package com.github.entwicklungsprojekt.shop.payload;

import com.github.entwicklungsprojekt.shop.persistence.Shop;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Object that holds {@link Shop} information that gets received from the client.
 */
@Data
@NoArgsConstructor
public class ShopPayload {

    /**
     * The Shop name.
     */
    String shopName;

    /**
     * The Shop location.
     */
    String shopLocation;

    /**
     * The Shop type.
     */
    String shopType;

}
