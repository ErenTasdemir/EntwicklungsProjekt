drop table if exists shop;

create table shop (

                      shop_id bigint(20) auto_increment not null primary key,
                      shop_name varchar(255) not null,
                      shop_type varchar(255) not null,
                      shop_location varchar(255) not null
);


insert into shop (shop_id, shop_name, shop_type, shop_location) values
(1, 'Bakery Crumble', 'baeckerei', 'bochum'),
(2, 'Bake Bake Bake', 'baeckerei', 'dortmund'),
(3, 'Fair and Wear', 'textilien', 'essen');
