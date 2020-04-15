package com.github.entwicklungsprojekt.shop.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;


import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Indexed
@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
