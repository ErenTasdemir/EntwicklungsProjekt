/*********************************
********* Shop_Location **********
*********************************/
drop table IF EXISTS shop_location;

create table  shop_location (
    location_id bigint(20) not null,
    shop_id bigint(20) not null,
    primary key ( location_id, shop_id),
    constraint location_id
        foreign key(location_id) references openstreetmap_location (id)
            on delete cascade on update cascade,
    constraint shop_id
        foreign key (shop_id) references shop (id)
            on delete cascade on update cascade
)
