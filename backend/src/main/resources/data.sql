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
(3, 'Fair and Wear', 'textilien', 'essen'),
(4, 'Food Schmood', 'lebensmittel', 'dortmund'),
(5, 'Toys Schmoys', 'spielwaren', 'koeln'),
(6, 'Couldn’t Wear Less', 'textilien', 'frankfurt am main'),
(7, 'Food of Sins', 'lebensmittel', 'essen und dortmund'),
(8, 'Cooking Schmooking', 'baeckerei', 'bochum harpen'),
(9, 'Eat Child O Mine', 'lebensmittel', 'norden'),
(10, 'Enjoy Enjoy Enjoy', 'spielwaren', 'hamburg'),
(11, 'In the Good Cooks', 'baeckerei', 'berlin, hamburg und essen'),
(12, 'Foodstuff Is Foodstuff', 'lebensmittel', 'karlsruhe'),
(13, 'Keep Your Wear on', 'textilien', 'hamm und dortmund'),
(14, 'Bad Wear Day', 'textilien', 'duesseldorf'),
(15, 'Sell Like Hot Bakes', 'baeckerei', 'gelsenkirchen'),
(16, 'Real Toy', 'spielwaren', 'dresden und leipzig'),
(17, 'In the Foodstuff', 'lebensmittel', 'stuttgart'),
(18, 'Make My Play', 'spielwaren', 'nuremberg'),
(19, 'Get the Snack', 'lebensmittel', 'hannover und essen'),
(20, 'Bake Back', 'baeckerei', 'dortmund, duesseldorf'),
(21, 'Come to Clothes', 'textilien', 'dortmund'),
(22, 'Off the Foodstuff', 'lebensmittel', 'essen'),
(23, 'Piece of Bake', 'baeckerei', 'dortmund'),
(24, 'Minced Clothes', 'textilien', 'koeln'),
(25, 'Set in Plays', 'spielwaren', 'frankfurt am main'),
(26, 'Toys Will Be Toys', 'spielwaren', 'essen und dortmund'),
(27, 'Snack Robinson', 'lebensmittel', 'bochum harpen'),
(28, 'Clothes Quarters', 'textilien', 'norden'),
(29, 'Icing on the Bake', 'baeckerei', 'hamburg'),
(30, 'Kick Your Meals', 'lebensmittel', 'berlin, hamburg und essen'),
(31, 'Blue Eyed Toy', 'spielwaren', 'karlsruhe'),
(32, 'Bake My Day', 'baeckerei', 'hamm und dortmund'),
(33, 'Snack the Ripper', 'lebensmittel', 'duesseldorf'),
(34, 'The Real Toy', 'spielwaren', 'gelsenkirchen'),
(35, 'Bake Breath Away', 'baeckerei', 'dresden und leipzig'),
(36, 'Textils Schmextils', 'textilien', 'stuttgart'),
(37, 'Bake Sense', 'baeckerei', 'nuremberg'),
(38, 'Foodstuff and Nonsense', 'lebensmittel', 'hannover und essen'),
(39, 'Eat Like a Horse', 'lebensmittel', 'dortmund, duesseldorf'),
(40, 'Fair Play', 'spielwaren', 'dortmund, duesseldorf');