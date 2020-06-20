package com.github.entwicklungsprojekt.shop.persistence;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import com.github.entwicklungsprojekt.user.persistence.User;
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
    @Column(name = "id", nullable = false)
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "shop_location",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id")
    )    Set<OpenstreetmapLocation> locations;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn( name = "user_id", nullable = false)
    User user;

    public Shop(String shopName, String shopLocation, String shopType, User user) {
        this.shopName = shopName;
        this.shopLocation = shopLocation;
        this.shopType = shopType;
        this.user = user;
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


}
