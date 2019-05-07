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
    category_name VARCHAR(22) NOT NULL,
    unit VARCHAR(12) NOT NULL
);

INSERT INTO categories_test (id, category_name, unit) VALUES
(1, "Træ", "Stk"),
(2, "Skruer", "Pakke");

CREATE TABLE products_test (
	id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(42) NOT NULL,
    category_id INT(8) NOT NULL,
    thickness INT(8) NOT NULL,
    width INT(8) NOT NULL,
    qty INT(8) NOT NULL,
    price DOUBLE,
    stock INT(8)
);

INSERT INTO products_test (id, product_name, category_id, thickness, width, qty, price, stock) VALUES
(1, "25x200 mm. trykimp. Brædt", 1, 25, 200, 1, 99.95, 99),
(2, "25x125 mm. trykimp. Brædt", 1, 25, 125, 1,  99.95, 99),
(3, "38x73 mm. Lægte ubh.", 38, 1, 73, 99.95, 1, 99),
(4, "45x95 mm. Reglar ub.", 45, 1, 95, 99.95, 1, 99),
(5, "45x195 mm. Spærtræ ubh.", 1, 45, 195, 99.95, 1, 99),
(6, "Plastmo Bundskruer 200 stk", 2, 0, 0, 200, 99.95, 99);

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