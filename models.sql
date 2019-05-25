CREATE TABLE projects (
	id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(42) NOT NULL
);

INSERT INTO projects (id, title) VALUES
(1, 'Carport');

CREATE TABLE models (
	id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    title VARCHAR(42) NOT NULL
);

INSERT INTO models (id, project_id, title) VALUES
(1, 1, 'Carport');

CREATE TABLE blueprints(
	id INT AUTO_INCREMENT PRIMARY KEY,
    usage_id INT NOT NULL,
    model_id INT NOT NULL,
    category_id INT NOT NULL,
    product_id INT NOT NULL,
    message VARCHAR(300) NOT NULL
);

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