SET MODE MYSQL;

/*********************************
************** User *************
*********************************/

create table if not exists user (
    id bigint(20) auto_increment not null primary key,
    user_password varchar(255) not null,
    user_firstname varchar(255) not null,
    user_lastname varchar(255) not null,
    username varchar(255) not null
);


/*********************************
************** Shop **************
*********************************/

create table if not exists shop (
    id bigint(20) auto_increment not null primary key,
    shop_name varchar(255) not null,
    shop_type varchar(255) not null,
    shop_location varchar(255) not null,
    user_id bigint(20) not null,
    constraint user_id foreign key (user_id) references user (id)
);


/*********************************
********** User_Roles ************
*********************************/
create table if not exists user_roles (
    user_id bigint not null,
    roles varchar(255)
);


/*********************************
***** Openstreetmap_Location *****
*********************************/

create table if not exists openstreetmap_location (
    id bigint(20) auto_increment not null primary key,
    name varchar(255) not null,
    latitude double not null,
    longitude double not null
);
