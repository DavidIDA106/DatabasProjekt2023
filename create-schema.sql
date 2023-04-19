drop table if exists Warehouses;
drop table if exists Cookies;
drop table if exists Recipes;
drop table if exists Pallets;
drop table if exists Orders;
drop table if exists Customers;
drop table if exists OrderSpecifications;

set FOREIGN_KEY_CHECKS=0;

create table Warehouses(
ingredientName varchar(50),
quantityInStock int,
unit varchar (20),
lastDeliveryAmount double (2,1),
lastDeliveryDate date,
primary key (ingredientName)
); 

create table Cookies(
cookieName varchar (50),
primary key (cookieName)
);

create table Recipes(
ingredientName varchar(50),
cookieName varchar(50),
amount double(2,1),
primary key (ingredientName, cookieName),
foreign key (ingredientName) references Warehouses (ingredientName),
foreign key (cookieName) references Cookies(cookieName)
);

create table Pallets(
orderID int,
cookieName varchar(50),
palletId int auto_increment,
blockedStatus boolean,
productionDate datetime,
location varchar(50),
deliveryDate datetime,
primary key (palletId),
foreign key (cookieName) references Cookies(cookieName),
foreign key (orderId) references Orders (orderId)
);

create table Orders(
customerName varchar(50),
orderId int auto_increment,
orderDateTime datetime,
primary key (orderId),
foreign key(customerName) references Customers(customerName)
);

create table Customers(
customerName varchar(50),
address varchar(100),
primary key(customerName)
);

create table OrderSpecification(
nbrPallets int,
orderId int,
cookieName varchar(50),
primary key (cookieName,orderId),
foreign key (orderId) references Orders(orderId),
foreign key (cookieName) references Cookies(cookieName)
);

set FOREIGN_KEY_CHECKS=1;