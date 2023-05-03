use hbg37;
set FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Recipes;
TRUNCATE TABLE Cookies;
TRUNCATE TABLE Warehouses;
TRUNCATE TABLE Pallets;
TRUNCATE TABLE Customers;
TRUNCATE TABLE Orders;


insert into Cookies (cookieName) VALUES
("Almond delight"),
("Amneris"),
("Berliner"),
("Nut cookie"),
("Nut Ring"),
("Tango");

insert into Warehouses (ingredientName, quantityInStock, unit) VALUES
("Bread Crumbs", 500000, "g"),
("Butter", 500000, "g"),
("Chocolate", 500000, "g"),
("Chopped almonds", 500000, "g"),
("Cinnamon", 500000, "g"),
("Egg whites", 500000, "ml"),
("Eggs", 500000, "g"),
("Fine-ground nuts", 500000, "g"),
("Flour", 500000, "g"),
("Ground, roasted nuts", 500000, "g"),
("Icing sugar", 500000, "g"),
("Marzipan", 500000, "g"),
("Potato starch", 500000, "g"),
("Roasted, chopped nuts", 500000, "g"),
("Sodium bicarbonate", 500000, "g"),
("Sugar", 500000, "g"),
("Vanilla Sugar", 500000, "g"),
("Vanilla", 500000, "g"),
("Wheat Flour", 500000, "g");

insert into Recipes (cookieName, ingredientName, amount) VALUES
("Almond delight", "Butter", 400),
("Almond delight", "Chopped Almonds", 279),
("Almond delight", "Cinnamon", 10),
("Almond delight", "Flour", 400),
("Almond delight", "Sugar", 270),
("Amneris", "Butter", 250),
("Amneris", "Eggs", 250),
("Amneris", "Marzipan", 750),
("Amneris", "Potato Starch", 25),
("Amneris", "Wheat Flour", 25),
("Berliner", "Butter", 250),
("Berliner", "Chocolate", 50),
("Berliner", "Egg whites", 350),
("Berliner", "Fine-ground nuts", 750),
("Berliner", "Ground, roasted nuts", 625),
("Berliner", "Sugar", 375),
("Nut cookie", "Bread crumbs", 125),
("Nut cookie", "Chocolate", 50),
("Nut cookie", "Egg whites", 350),
("Nut cookie", "Fine-ground nuts", 750),
("Nut cookie", "Ground, roasted nuts", 625),
("Nut cookie", "Sugar", 375),
("Nut ring", "Butter", 450),
("Nut ring", "Flour", 450),
("Nut ring", "Icing sugar", 190),
("Nut ring", "Roasted, chopped nuts", 225),
("Tango", "Butter", 200),
("Tango", "Flour", 300),
("Tango", "Sodium bicarbonate", 4),
("Tango", "Sugar", 250),
("Tango", "Vanilla", 2);









insert into Customers (customerName, address) VALUES
("Finkakor AB", "Helsingborg"),
("Småbröd AB", "Malmö"),
("Kaffebröd AB", "Landskrona"),
("Bjudkakor AB", "Ystad"),
("Kalaskakor AB", "Trelleborg"),
("Partykakor AB", "Kristianstad"),
("Gästkakor AB", "Hässleholm"),
("Skånekakor AB", "Perstorp");

set FOREIGN_KEY_CHECKS = 1;