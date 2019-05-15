use carports;


INSERT INTO customer (cname, email, address, zip, phone) VALUES
("Anders Andersen","aa@etyrytre.dk", "Avej 1", 3000, 11111111),
("Børge Børgesen","bb@ereokre.dk", "Bvej 2", 3050, 22222222)
;


INSERT INTO c_order (height, length, width, shed_length, shed_width, roof_angle, roof_type, cust_id) VALUES
(300,720,360,330,210,25,0, 1),
(305,630,360,0,210,0,9, 2),
(305,720,360,330,210,25,0, 2),
(305,630,360,0,210,0,9, 2),
(305,720,360,330,210,25,0, 2),
(305,630,360,0,210,0,9, 1),
(305,720,360,330,210,25,0, 1),
(305,630,360,0,210,0,9, 1),
(305,720,360,330,210,25,0, 1),
(305,630,360,0,210,0,9, 1),
(305,720,360,330,210,25,0, 1),
(305,630,360,0,210,0,9, 1),
(305,720,360,330,210,25,0, 2),
(305,630,360,0,210,0,9, 1)
;
