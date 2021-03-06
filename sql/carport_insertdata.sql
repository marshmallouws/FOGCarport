USE carports;
INSERT INTO employee (id, initials, passw) VALUES
(1, "aaa", "aaa");

INSERT INTO categories (id, category_name) VALUES
(1, "Stolper"),
(2, "Remme"),
(3, "Understernbrædder"),
(4, "Oversternbrædder"),
(5, "Beklædning"),
(6, "Vandbræt"),
(7, "Tagplader"),
(8, "Spær"),
(9, "Bundskruer"),
(10, "Universal"),
(11, "Skruer");

INSERT INTO products (id, product_name, thickness, width) VALUES
(1, "25x200 mm. trykimp. Brædt", 25, 200),
(2, "25x125 mm. trykimp. Brædt", 25, 125),
(3, "38x73 mm. Lægte ubh.", 38, 73),
(4, "45x95 mm. Reglar ub.", 45, 95),
(5, "45x195 mm. Spærtræ ubh.", 45, 195),
(6, "Plastmo Bundskruer 200 stk", 0, 0),
(7, "97x97 mm. trykimp. Stolpe", 97, 97),
(8, "19x100 mm. trykimp. Brædt", 19, 100),
(9, "Plastmo Ecolite blåtonet", 5, 90),
(10, "Plastmo Ecolite grøntonet", 5, 900),
(11, "Plastmo Ecolite rødtonet", 5, 900),
(12, "Plastmo Ecolite klar", 5, 900),
(13, "Plastmo bundskruer", 0, 0),
(14, "Universal 190 mm", 0, 190),
(15, "4,5 Skruer", 0, 4.5),
(16, "4,0 Skruer", 0, 4.0);

INSERT INTO products_in_categories (category_id, product_id) VALUES
	(1, 7),
    (2, 5),
    (3, 1),
    (4, 2),
    (5, 8),
    (6, 8),
    (7, 9),
    (7, 10),
    (7, 11),
    (7, 12),
    (8, 5),
    (11, 13),
    (11, 14),
    (11, 15),
    (11, 16);
    
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
(61, 9, 150, 59.95, 888),
(62, 9, 180, 59.95, 888),
(63, 9, 210, 59.95, 888),

#ekstra remme
(64, 5, 240, 199.95, 888),
(65, 5, 630, 199.95, 888),
(66, 5, 660, 199.95, 888),
(67, 5, 690, 199.95, 888),
(68, 5, 720, 199.95, 888),
(69, 5, 750, 199.95, 888),

#ekstra tagplader
(70, 10, 150, 59.95, 888),
(71, 10, 180, 59.95, 888),
(72, 10, 210, 59.95, 888),
(73, 11, 150, 59.95, 888),
(74, 11, 180, 59.95, 888),
(75, 11, 210, 59.95, 888),
(76, 12, 150, 39.99, 888),
(77, 12, 180, 39.99, 888),
(78, 12, 210, 39.99, 888),

#skruer
(79, 13, 60, 0.45, 888),
(80, 14, 50, 0.25, 888),
(81, 15, 50, 0.13, 888),
(82, 15, 70, 0.15, 888),
(83, 16, 50, 0.11, 888);

INSERT INTO projects (id, title) VALUES
(1, 'Carport');

INSERT INTO models (id, project_id, title) VALUES
(1, 1, 'Carport Model 1');

INSERT INTO blueprints (id, usage_id, model_id, category_id, product_id, message) VALUES
(1, 1, 1, 1, 7, "Stolper"),
(2, 2, 1, 2, 5, "Remme i sider, sadles ned i stolper"),
(3, 3, 1, 8, 5, "Spær"),
(4, 4, 1, 3, 1, "Understernbrædder siderne"),
(5, 5, 1, 3, 1, "Understernbrædder for- og bagende"),
(6, 6, 1, 7, 12, "Tagplader monteres på spær"),
(7, 7, 1, 7, 12, "Tagsten monteres på spær med hældning"),
(8, 8, 1, 8, 5, "Spær til taget"),
(9, 9, 1, 11, 13, "Skruer til tagplader"),
(10, 10, 1, 5, 8, "Til beklædning af skur siderne"),
(11, 11, 1, 5, 8, "Til beklædning af skur gavle"),
(12, 12, 1, 11, 15, "Til montering af yderste beklædning"),
(13, 13, 1, 11, 15, "Til montering af inderste beklædning");