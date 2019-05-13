/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.sun.org.apache.bcel.internal.generic.Select;
import entity.Category;
import entity.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Generated;

/**
 *
 * @author Annika
 */
public class ProductMapper implements ProductDAOInterface {

    @Override
    public void insertNewProduct(Product product, Category category) {
        try {
            Connection con = Connector.connection();
            String query = "INSERT INTO product (cat_id, height, length, width, price, stock) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, category.getId());
            ps.setInt(2, product.getHeight());
            ps.setInt(3, product.getLength());
            ps.setInt(4, product.getWidth());
            ps.setDouble(5, product.getPrice());
            ps.setInt(6, product.getStock());
            ps.executeUpdate();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<Product> getProducts() {
        ArrayList<Product> p = new ArrayList<>();
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM product;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int catId = rs.getInt("cat_id");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                double price = rs.getDouble("price");
                boolean active = rs.getBoolean("active");
                int stock = rs.getInt("stock");

                Category cat = getCategory(catId);

                p.add(new Product(id, 0, cat, height, length, width, price, active, stock, "")); // bruger gamle db, men ny constructor. skal laves om :)
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return p;
    }

    private Category getCategory(int id) {
        Category cat = null;
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM category WHERE id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("cat_name");
                boolean height = rs.getBoolean("height");
                boolean length = rs.getBoolean("width");
                boolean width = rs.getBoolean("width");

                cat = new Category(id, name, height, width, length);
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return cat;
    }

    @Override
    public Product getProduct(int id) {
        Product product = null;
        try {
            Connection con = Connector.connection();
            //String query = "SELECT * FROM product WHERE id = ?";
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, active, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE product_variants.id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
//                Category c = getCategory(rs.getInt("cat_id"));
//                product = new Product(id, 0, c, rs.getInt("height"), rs.getInt("length"),
//                        rs.getInt("width"), rs.getDouble("price"), rs.getBoolean("active"),
//                        rs.getInt("stock"), ""); // bruger gamle db, men ny constructor. skal laves om :)

                product = new DevMapper().buildProduct(rs);
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return product;
    }

    @Override
    public ArrayList<Product> getProducts(Category category) {
        ArrayList<Product> p = new ArrayList<>();
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM product WHERE cat_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, category.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int catId = rs.getInt("cat_id");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                double price = rs.getDouble("price");
                boolean active = rs.getBoolean("active");
                int stock = rs.getInt("stock");

                Category cat = getCategory(catId);

                p.add(new Product(id, 0, cat, height, length, width, price, active, stock, "")); // bruger gamle db, men ny constructor. skal laves om :)
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return p;
    }

    @Override
    public void setActivity(Product product) {
        try {
            Connection con = Connector.connection();
            String query = "UPDATE product SET active = ? WHERE id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setBoolean(1, !(product.isActive()));
            ps.setInt(2, product.getId());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<Product> getProducts(boolean activity) {
        ArrayList<Product> p = new ArrayList<>();
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM product WHERE active = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setBoolean(1, activity);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int catId = rs.getInt("cat_id");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                double price = rs.getDouble("price");
                boolean active = rs.getBoolean("active");
                int stock = rs.getInt("stock");

                Category cat = getCategory(catId);

                p.add(new Product(id, 0, cat, height, length, width, price, active, stock, "")); // bruger gamle db, men ny constructor. skal laves om :)
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return p;
    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> cat = new ArrayList();
        try {
            Connection con = Connector.connection();
            //String query = "SELECT * FROM category;";
            String query = "SELECT id, category_name FROM categories_test;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
//                int id = rs.getInt("id");
//                String name = rs.getString("cat_name");
//                boolean height = rs.getBoolean("height");
//                boolean length = rs.getBoolean("width");
//                boolean width = rs.getBoolean("width");

                int id = rs.getInt("id");
                String name = rs.getString("category_name");
                boolean height = true;
                boolean length = true;
                boolean width = true;

                cat.add(new Category(id, name, height, width, length));
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return cat;
    }

    public ArrayList<Product> getProductList(int category_id) {
        ArrayList<Product> prod = new ArrayList();
        try {
            Connection con = Connector.connection();
            //String query = "SELECT * FROM product WHERE cat_id=?;";
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, category_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
//                int id = rs.getInt("id");
//                int height = rs.getInt("height");
//                int length = rs.getInt("width");
//                int width = rs.getInt("width");
//                double price = rs.getDouble("price");
//                boolean active = rs.getBoolean("active");
//                int stock = rs.getInt("stock");

                prod.add(new DevMapper().buildProduct(rs));

                //prod.add(new Product(id, 0, getCategory(category_id), height, length, width, price, active, stock, "")); // bruger gamle db, men ny constructor. skal laves om :)
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return prod;
    }

    public ArrayList<Product> getProductVariantsList(int categoryID, int productID) {
        ArrayList<Product> prod = new ArrayList();
        try {
            Connection con = Connector.connection();
            //String query = "SELECT * FROM product WHERE cat_id=?;";
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, active, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ? AND products_in_categories.product_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, categoryID);
            ps.setInt(2, productID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
//                int id = rs.getInt("id");
//                int height = rs.getInt("height");
//                int length = rs.getInt("width");
//                int width = rs.getInt("width");
//                double price = rs.getDouble("price");
//                boolean active = rs.getBoolean("active");
//                int stock = rs.getInt("stock");

                prod.add(new DevMapper().buildProduct(rs));

                //prod.add(new Product(id, 0, getCategory(category_id), height, length, width, price, active, stock, "")); // bruger gamle db, men ny constructor. skal laves om :)
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return prod;
    }
    
    public List<Product> getProductsInCategories(int categoryID) {
        List<Product> products = new ArrayList();
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM products_in_categories\n"
                    + "JOIN products_test ON products_test.id = products_in_categories.product_id\n"
                    + "JOIN categories_test ON categories_test.id = products_in_categories.category_id\n"
                    + "WHERE category_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, categoryID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(rs.getInt("product_id"), new Category(rs.getInt("category_id"), rs.getString("category_name")), rs.getString("product_name")));
            }
            return products;
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        return products;
    }

    public boolean saveProduct(Product product) {
        try {
            Connection con = Connector.connection();
            String query = "UPDATE product SET "
                    + "height=?,length=?,width=?,price=?,active=? "
                    + "WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, product.getHeight());
            ps.setInt(2, product.getLength());
            ps.setInt(3, product.getWidth());
            ps.setDouble(4, product.getPrice());
            ps.setBoolean(5, product.isActive());
            ps.setInt(6, product.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public int createProduct(Product product) {
        try {
            int id = 0;
            Connection con = Connector.connection();
            con.setAutoCommit(false); // implement transactions with categoryID
            String query = "INSERT INTO products_test (product_name, thickness, width) VALUES (?,?,?);";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setInt(2, product.getThickness());
            ps.setInt(3, product.getWidth());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()) {
                id = rs.getInt(1);
                
                query = "INSERT INTO";
                
            }
            
            return id;
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    public boolean updateProductVariant(Product product) {
        try {
            Connection con = Connector.connection();
            String query = "UPDATE product_variants SET price = ?, stock = ?, active = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, product.getPrice());
            ps.setInt(2, product.getStock());
            ps.setBoolean(3, product.isActive());
            ps.setInt(4, product.getVariant_id()); //
            ps.executeUpdate();
            return true;
            
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Product> getRoofTypes() {
        List<Product> roofs = new ArrayList();

        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM products_in_categories\n"
                    + "JOIN products_test ON products_in_categories.product_id = products_test.id\n"
                    + "WHERE category_id = 7;";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                roofs.add(new Product(rs.getInt("product_id"), rs.getString("product_name")));
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        return roofs;
    }

}
