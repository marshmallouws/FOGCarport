DROP SCHEMA IF EXISTS carports;
CREATE SCHEMA carports;
USE carports;

CREATE TABLE c_user(
	id INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(20) NOT NULL,
    pass VARCHAR(20) NOT NULL
);

CREATE TABLE c_order(
	id INT AUTO_INCREMENT PRIMARY KEY,
    height INT NOT NULL,
    length INT NOT NULL,
    width INT NOT NULL,
    shed_length INT,
    shed_width INT,
    roof_angle INT,
    userid INT,
    o_date DATETIME, 
    #Reference to employee handling the order
	CONSTRAINT c_user_c_order
		FOREIGN KEY(userid)
        REFERENCES c_user(id)
);