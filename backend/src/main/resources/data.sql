drop table if exists shop;

create table shop (

    shop_id bigint(20) auto_increment not null primary key,
    shop_name varchar(255) not null,
    shop_type varchar(255) not null
);
