package com.github.entwicklungsprojekt.openstreetmap_location.persistence;

import com.github.entwicklungsprojekt.shop.persistence.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity to persist OpenstreetmapLocation data.
 *
 * @see OpenstreetmapLocation
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "openstreetmap_location")
public class OpenstreetmapLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private GeoData geoData;



    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "shop_location",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id")
    )
    private Set<Shop> shops;

    public void addShop(Shop shop) {
        if (this.shops == null) {
            this.shops = new HashSet<>();
        }
        this.shops.add(shop);
    }


}
