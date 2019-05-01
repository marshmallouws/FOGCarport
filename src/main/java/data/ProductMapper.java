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

/**
 *
 * @author Annika
 */
public class ProductMapper implements ProductDAOInterface {

    @Override
    public void insertNewProduct(Product product, Category category) {
        try {
            Connection con = Connector.connection();
            String query = "INSERT INTO product (cat_id, height, length, width) " 
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, category.getId());
            ps.setInt(2, product.getHeight());
            ps.setInt(3, product.getLength());
            ps.setInt(3, product.getWidth());
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
            
            while(rs.next()) {
                int id = rs.getInt("id");
                int catId = rs.getInt("cat_id");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                double price = rs.getDouble("price");
                boolean active = rs.getBoolean("active");
                
                Category cat = getCategory(catId);
                
                p.add(new Product(id, cat, height, length, width, price, active));
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
            
            
            if(rs.next()) {
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
    public Product getProduct() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Product> getProducts(Category category) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
