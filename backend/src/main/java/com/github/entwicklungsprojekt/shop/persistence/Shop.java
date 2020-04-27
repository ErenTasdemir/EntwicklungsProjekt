package com.github.entwicklungsprojekt.shop.persistence;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Indexed
@NoArgsConstructor
@Getter
@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long shopId;

    @Column(name = "shop_name")
    @NotNull
    @Field
    String shopName;

    @Column(name = "shop_type")
    @NotNull
    @Field
    String shopType;

    @Column(name = "shop_location")
    @NotNull
    @Field
    String shopLocation;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "shop_image")
    private byte[] shopImage;

    @ManyToMany(mappedBy = "shops", fetch = FetchType.EAGER)
    Set<OpenstreetmapLocation> locations;

    public Shop(String shopName, String shopLocation, String shopType) {
        this.shopName = shopName;
        this.shopLocation = shopLocation;
        this.shopType = shopType;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public void setLocations(Set<OpenstreetmapLocation> locations) {
        this.locations = locations;
    }

    public void setShopImage(byte[] shopImage) {
        this.shopImage = shopImage;
    }
}
