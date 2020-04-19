package com.github.entwicklungsprojekt.shop.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopPayload {

    String shopName;

    String shopLocation;

    String shopType;

}
