DROP DATABASE IF EXISTS prototype;
CREATE DATABASE prototype;

USE prototype;

CREATE TABLE categories (
	id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(42) NOT NULL
);

INSERT INTO categories (id, category_name) VALUES
(1, "Remme"),
(2, "Stolper"),
(3, "Understernbrædder"),
(4, "Oversternbrædder"),
(5, "Løsholter"),
(6, "Spær"),
(7, "Beklædning"),
(8, "Tagplader");

CREATE TABLE products (
	id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(42) NOT NULL,
    thickness INT(8),
    width INT(8)
);

INSERT INTO products (id, product_name, thickness, width) VALUES
(1, "45x195 mm. spærtræ ubh", 45, 195),
(2, "97x97 mm. spærtræ ubh.", 97,97),
(3, "25x200 mm. trykimp. Brædt", 25, 200);

CREATE TABLE products_variants (
	id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT(8) NOT NULL,
    category_id INT(8),
    length INT(8),
    price DOUBLE,
    stock INT(8)
);

INSERT INTO products_variants (id, product_id, category_id, length, price, stock) VALUES
(1, 1, 1, 600, 888.95, 99),
(2, 1, 1, 480, 699.95, 99),
(3, 2, 2, 300, 199,95, 99);