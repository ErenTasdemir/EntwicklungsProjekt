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


/**
 * Entity to persist {@link Shop} data.
 */
@Indexed
@NoArgsConstructor
@Getter
@Entity
@Table(name = "shop")
public class Shop {

    /**
     * The Shop id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long shopId;

    /**
     * The Shop name.
     */
    @Column(name = "shop_name")
    @NotNull
    @Field
    String shopName;

    /**
     * The Shop type.
     */
    @Column(name = "shop_type")
    @NotNull
    @Field
    String shopType;

    /**
     * The Shop location.
     */
    @Column(name = "shop_location")
    @NotNull
    @Field
    String shopLocation;

    /**
     * The Locations.
     */
    @ManyToMany(mappedBy = "shops", fetch = FetchType.EAGER)
    Set<OpenstreetmapLocation> locations;

    /**
     * The User.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn( name = "user_id", nullable = false)
    User user;

    @PreRemove
    private void removeShopFromUser() {
        user.getShops().remove(this);
    }

    /**
     * Instantiates a new Shop.
     *
     * @param shopName     the shop name
     * @param shopLocation the shop location
     * @param shopType     the shop type
     * @param user         the user
     */
    public Shop(String shopName, String shopLocation, String shopType, User user) {
        this.shopName = shopName;
        this.shopLocation = shopLocation;
        this.shopType = shopType;
        this.user = user;
    }

    /**
     * Sets shop name.
     *
     * @param shopName the shop name
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * Sets shop type.
     *
     * @param shopType the shop type
     */
    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    /**
     * Sets shop location.
     *
     * @param shopLocation the shop location
     */
    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    /**
     * Sets locations.
     *
     * @param locations the locations
     */
    public void setLocations(Set<OpenstreetmapLocation> locations) {
        this.locations = locations;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
