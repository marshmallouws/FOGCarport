/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Category;
import entity.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

                p.add(new Product(id, cat, height, length, width, price, active, stock));
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
            String query = "SELECT * FROM product WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Category c = getCategory(rs.getInt("cat_id"));
                product = new Product(id, c, rs.getInt("height"), rs.getInt("length"),
                        rs.getInt("width"), rs.getDouble("price"), rs.getBoolean("active"),
                        rs.getInt("stock"));
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ProductMapper.class.getName()).log(Level.SEVERE, null, ex);
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

                p.add(new Product(id, cat, height, length, width, price, active, stock));
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

                p.add(new Product(id, cat, height, length, width, price, active, stock));
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
            String query = "SELECT * FROM category;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("cat_name");
                boolean height = rs.getBoolean("height");
                boolean length = rs.getBoolean("width");
                boolean width = rs.getBoolean("width");

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
            String query = "SELECT * FROM product WHERE cat_id=?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, category_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int height = rs.getInt("height");
                int length = rs.getInt("width");
                int width = rs.getInt("width");
                double price = rs.getDouble("price");
                boolean active = rs.getBoolean("active");
                int stock = rs.getInt("stock");

                prod.add(new Product(id, getCategory(category_id), height, length, width, price, active, stock));
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return prod;
    }

    public boolean saveProduct(Product product) {
        try {
            Connection con = Connector.connection();
            String query = "UPDATE product SET "+
                    "height=?,length=?,width=?,price=?,active=? "+
                    "WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, product.getHeight());
            ps.setInt(2, product.getLength());
            ps.setInt(3, product.getWidth());
            ps.setDouble(4, product.getPrice());
            ps.setBoolean(5,product.isActive());
            ps.setInt(6, product.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
