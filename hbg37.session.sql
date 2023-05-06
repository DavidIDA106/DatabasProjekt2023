set FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Recipes;
TRUNCATE TABLE Cookies;
TRUNCATE TABLE Warehouses;
TRUNCATE TABLE Pallets;
TRUNCATE TABLE Customers;
TRUNCATE TABLE Orders;
set FOREIGN_KEY_CHECKS = 1;
SELECT palletId AS id, cookieName AS cookie, productionDate AS production_date, Customers.customerName AS customer, blockedStatus as blocked 
FROM Pallets 
LEFT JOIN Customers ON Customers.customerName = Pallets.customerName
