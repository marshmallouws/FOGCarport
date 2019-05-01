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

CREATE TABLE customer(
	id INT AUTO_INCREMENT PRIMARY KEY,
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

CREATE TABLE product(
	id INT AUTO_INCREMENT PRIMARY KEY,
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
