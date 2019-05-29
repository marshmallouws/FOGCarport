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
    cname VARCHAR(45) NOT NULL,
    email VARCHAR(20) UNIQUE NOT NULL,
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
    roof_type INT,
    emp_id INT,
    cust_id INT NOT NULL,
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

CREATE TABLE categories (
	id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(22) NOT NULL
);

CREATE TABLE products (
	id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(42) NOT NULL,
    thickness INT(8) NOT NULL,
    width INT(8) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE products_in_categories (
	category_id INT(8) NOT NULL,
    product_id INT(8) NOT NULL,
    CONSTRAINT pic_category
		FOREIGN KEY(category_id)
        REFERENCES categories(id),
	CONSTRAINT pic_product 
		FOREIGN KEY(product_id)
        REFERENCES products(id)
);

CREATE TABLE product_variants (
	id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT(8) NOT NULL,
    length INT(8) NOT NULL,
    price DOUBLE,
    stock INT(8),
    active BOOLEAN DEFAULT TRUE,
    CONSTRAINT pv_products
		FOREIGN KEY(product_id)
        REFERENCES products(id)
);

CREATE TABLE odetail(
	id INT AUTO_INCREMENT PRIMARY KEY,
    prod_id INT NOT NULL,
    order_id INT NOT NULL,
    qty INT NOT NULL,
    amount DOUBLE NOT NULL,
    cmt VARCHAR(250) NOT NULL,
    CONSTRAINT prod_odetail
		FOREIGN KEY(prod_id)
        REFERENCES product_variants(id),
	CONSTRAINT order_odetail
		FOREIGN KEY(order_id)
        REFERENCES c_order(id)
);

-- NOT IMPL.
CREATE TABLE projects (
	id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(42) NOT NULL
);

CREATE TABLE models (
	id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    title VARCHAR(42) NOT NULL,
    CONSTRAINT models_project_id
		FOREIGN KEY(project_id)
        REFERENCES projects(id)
);

CREATE TABLE blueprints(
	id INT AUTO_INCREMENT PRIMARY KEY,
    usage_id INT NOT NULL,
    model_id INT NOT NULL,
    category_id INT NOT NULL,
    product_id INT NOT NULL,
    message VARCHAR(300) NOT NULL,
    CONSTRAINT blueprint_model_id
		FOREIGN KEY(model_id)
        REFERENCES models(id),
	CONSTRAINT blueprint_category_id
		FOREIGN KEY(category_id)
        REFERENCES categories(id),
	CONSTRAINT blueprint_product_id
		FOREIGN KEY(product_id)
        REFERENCES products(id)
);