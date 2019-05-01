use carports;

INSERT INTO category (`cat_name`,`height`,`length`,`width`) values
('Bræder',true,true,true),
('Søm',false,false,false);

INSERT INTO product (cat_id,height,length,width,price,stock,active) values
(1,5,25,2,20.95,240,true),
(1,5,10,2,42.45,256,true),
(1,2,10,3,44.45,277,true),
(1,2,25,6,52.45,299,true),
(1,25,25,6,62.45,57,true),
(2,null,null,null,2.45,1050,true),
(2,null,null,null,2.95,2948,true);