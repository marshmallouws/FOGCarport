DROP DATABASE IF EXISTS carports;
CREATE DATABASE carports;

USE carports;

CREATE TABLE zipcodes(
	zip INT(4) PRIMARY KEY,
    city VARCHAR(30) NOT NULL
);

CREATE TABLE employee(
	id INT AUTO_INCREMENT PRIMARY KEY,
    initials VARCHAR(3) NOT NULL,
    passw VARCHAR(20) NOT NULL
);

INSERT INTO employee (id, initials, passw) VALUES
(1, "aaa", "aaa");

CREATE TABLE customer(
	id INT AUTO_INCREMENT PRIMARY KEY,
    cname VARCHAR(45) NOT NULL,
    email VARCHAR(20) UNIQUE NOT NULL,
	#passw VARCHAR(20) NOT NULL,
    address VARCHAR(30) NOT NULL,
    zip INT(4) NOT NULL,
    phone INT(8) NOT NULL,
    CONSTRAINT zip_customer
		FOREIGN KEY (zip)
        REFERENCES zipcodes(zip)
);

CREATE TABLE c_order(
	id INT AUTO_INCREMENT PRIMARY KEY,
    height INT NOT NULL,
    length INT NOT NULL,
    width INT NOT NULL,
    shed_length INT,
    shed_width INT,
    roof_angle INT,
    emp_id INT,
    cust_id INT, #Needs to be changed to 'not null'
    o_date DATETIME DEFAULT NOW(),
    o_status ENUM('recieved', 'delivered') DEFAULT 'recieved',
    sales_price DOUBLE, #Price given by employee
    #Reference to employee handling the order
	CONSTRAINT empl_c_order
		FOREIGN KEY(emp_id)
        REFERENCES employee(id),
	CONSTRAINT cust_c_order
		FOREIGN KEY(cust_id)
        REFERENCES customer(id)
);

CREATE TABLE category(
	id INT AUTO_INCREMENT PRIMARY KEY,
    cat_name VARCHAR(30) NOT NULL,
    height BOOLEAN,
    length BOOLEAN,
    width BOOLEAN
    # Booleans are for checking whether or not the given measurement is required for inserting new element
);

INSERT INTO category (id, cat_name, height, length, width) VALUES
(1, "Stolper", true, true, true),
(2, "Remme", true, true, true),
(3, "Sternbrædder", true, true, true),
(4, "Understernbrædder", true, true, true),
(5, "Oversternbrædder", true, true, true),
(6, "Beklædning", true, true, true ),
(7, "Spær", true, true, true),
(8, "Tagplader", true, true, true);

CREATE TABLE product(
	id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(42) NOT NULL,
    cat_id INT NOT NULL,
    height INT,
    length INT,
    width INT,
    price DOUBLE,
    stock INT,
    active BOOLEAN DEFAULT TRUE,
    CONSTRAINT cat_prod
		FOREIGN KEY(cat_id)
        REFERENCES category(id)
);

INSERT INTO product (id, product_name, cat_id, height, length, width, price, stock, active) VALUES
(1, "97x97 mm. trykimp. Stolpe", 1, 0, 300, 97, 888.95, 99, true),
(2, "45x195 mm. spærtræ ubh.", 2, 0, 600, 195, 888.95, 99, true),
(3, "45x195 mm. spærtræ ubh.", 2, 0, 480, 195, 888.95, 99, true),
(4, "19x100 mm. trykimp. Brædt", 2, 0, 480, 195, 888.95, 99, true);

CREATE TABLE categories_test (
	id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(22) NOT NULL
);

INSERT INTO categories_test (id, category_name) VALUES
(1, "Stolper"),
(2, "Remme"),
(3, "Understernbrædder"),
(4, "Oversternbrædder"),
(5, "Beklædning"),
(6, "Vandbræt"),
(7, "Tagplader"),
(8, "Spær");

CREATE TABLE products_test (
	id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(42) NOT NULL,
    thickness INT(8) NOT NULL,
    width INT(8) NOT NULL
);

INSERT INTO products_test (id, product_name, thickness, width) VALUES
(1, "25x200 mm. trykimp. Brædt", 25, 200),
(2, "25x125 mm. trykimp. Brædt", 25, 125),
(3, "38x73 mm. Lægte ubh.", 38, 73),
(4, "45x95 mm. Reglar ub.", 45, 95),
(5, "45x195 mm. Spærtræ ubh.", 45, 195),
(6, "Plastmo Bundskruer 200 stk", 0, 0),
(7, "97x97 mm. trykimp. Stolpe", 97, 97),
(8, "19x100 mm. trykimp. Brædt", 19, 100),
(9, "Plastmo Ecolite blåtonet", 5, 90);

CREATE TABLE products_in_categories (
	category_id INT(8) NOT NULL,
    product_id INT(8) NOT NULL
);

INSERT INTO products_in_categories (category_id, product_id) VALUES
	(1, 7),
    (2, 5),
    (3, 1),
    (4, 2),
    (5, 8),
    (6, 8),
    (7, 9),
    (8, 5);

CREATE TABLE product_variants (
	id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT(8) NOT NULL,
    length INT(8) NOT NULL,
    price DOUBLE,
    stock INT(8)
);

INSERT INTO product_variants (id, product_id, length, price, stock) VALUES
# understernbrædder
(1, 1, 270, 99.95, 888),
(2, 1, 300, 99.95, 888),
(3, 1, 330, 99.95, 888),
(4, 1, 360, 99.95, 888),
(5, 1, 390, 99.95, 888),
(6, 1, 420, 99.95, 888),
(7, 1, 450, 99.95, 888),
(8, 1, 480, 99.95, 888),
(9, 1, 510, 99.95, 888),
(10, 1, 540, 99.95, 888),
(11, 1, 570, 99.95, 888),
(12, 1, 600, 99.95, 888),

#oversternbrædder
(13, 2, 270, 89.99, 888),
(14, 2, 300, 89.99, 888),
(15, 2, 330, 89.99, 888),
(16, 2, 360, 89.99, 888),
(17, 2, 390, 89.99, 888),
(18, 2, 420, 89.99, 888),
(19, 2, 450, 89.99, 888),
(20, 2, 480, 89.99, 888),
(21, 2, 510, 89.99, 888),
(22, 2, 540, 89.99, 888),
(23, 2, 570, 89.99, 888),
(24, 2, 600, 89.99, 888),

#remme
(25, 5, 270, 199.95, 888),
(26, 5, 300, 199.95, 888),
(27, 5, 330, 199.95, 888),
(28, 5, 360, 199.95, 888),
(29, 5, 390, 199.95, 888),
(30, 5, 420, 199.95, 888),
(31, 5, 450, 199.95, 888),
(32, 5, 480, 199.95, 888),
(33, 5, 510, 199.95, 888),
(34, 5, 540, 199.95, 888),
(35, 5, 570, 199.95, 888),
(36, 5, 600, 199.95, 888),

#stolper
(37, 7, 270, 49.99, 888),
(38, 7, 300, 49.99, 888),
(39, 7, 330, 49.99, 888),
(40, 7, 360, 49.99, 888),
(41, 7, 390, 49.99, 888),
(42, 7, 420, 49.99, 888),
(43, 7, 450, 49.99, 888),
(44, 7, 480, 49.99, 888),
(45, 7, 510, 49.99, 888),
(46, 7, 540, 49.99, 888),
(47, 7, 570, 49.99, 888),
(48, 7, 600, 49.99, 888),

#beklædning #vandbræt
(49, 8, 270, 78.98, 888),
(50, 8, 300, 78.98, 888),
(51, 8, 330, 78.98, 888),
(52, 8, 360, 78.98, 888),
(53, 8, 390, 78.98, 888),
(54, 8, 420, 78.98, 888),
(55, 8, 450, 78.98, 888),
(56, 8, 480, 78.98, 888),
(57, 8, 510, 78.98, 888),
(58, 8, 540, 78.98, 888),
(59, 8, 570, 78.98, 888),
(60, 8, 600, 78.98, 888),

#tagplader
(61, 9, 150, 39.99, 888),
(62, 9, 180, 39.99, 888),
(63, 9, 210, 39.99, 888),

#ekstra remme
(64, 5, 240, 199.95, 888),
(65, 5, 630, 199.95, 888),
(66, 5, 660, 199.95, 888),
(67, 5, 690, 199.95, 888),
(68, 5, 720, 199.95, 888),
(69, 5, 750, 199.95, 888);

CREATE TABLE odetail(
	id INT AUTO_INCREMENT PRIMARY KEY,
    prod_id INT NOT NULL,
    order_id INT NOT NULL,
    amount INT NOT NULL,
    CONSTRAINT prod_odetails
		FOREIGN KEY(prod_id)
        REFERENCES product(id),
	CONSTRAINT order_odetail
		FOREIGN KEY(order_id)
        REFERENCES c_order(id)
);