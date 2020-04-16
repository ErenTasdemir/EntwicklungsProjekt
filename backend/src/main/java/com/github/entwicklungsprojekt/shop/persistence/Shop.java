package com.github.entwicklungsprojekt.shop.persistence;

import com.github.entwicklungsprojekt.openstreetmap_location.persistence.OpenstreetmapLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Indexed
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
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

    @ManyToMany(mappedBy = "shops", fetch = FetchType.EAGER)
    Set<OpenstreetmapLocation> locations;

}
