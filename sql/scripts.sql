# Alle varianter af et produkt
-- SELECT * FROM carports.product_variants JOIN products_test ON product_variants.product_id = products_test.id WHERE product_id = 1;

# Alle varianter af produkter i en category
-- SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id JOIN products_test ON product_variants.product_id = products_test.id WHERE category_id = 2;

# Variant af produkt i en kategori med bestemt længde
-- SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id JOIN products_test ON product_variants.product_id = products_test.id WHERE category_id = 1 AND length = 420;