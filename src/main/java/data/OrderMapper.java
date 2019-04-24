/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Order;
import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bitten
 */
public class OrderMapper implements OrderInterface {

    @Override
    public void createOrder(Order order) {
        try {
            Connection con = Connector.getConnection();
            String SQL = "INSERT INTO `user` (height, length, width, shed_length, shed_width, roof_angle, userid, o_date) VALUES (?, ?, ?, ?, ?, ?, ?, now())";
            PreparedStatement ps = con.prepareStatement( SQL );
            ps.setInt( 1, order.getHeight() );
            ps.setInt( 2, order.getLenght() );
            ps.setInt( 3, order.getWidth() );
            ps.setInt( 4, order.getShedLength() );
            ps.setInt( 5, order.getShedWidth() );
            ps.setInt( 6, order.getRoofAngle() );
            ps.setInt( 7, 0 );
            ps.executeUpdate();
            } catch ( SQLException ex ) {

            }
    }

    @Override
    public ArrayList<Order> getOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            Connector c = new Connector();
            Connection con = c.getConnection();
            String query = "SELECT * FROM c_order";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                int shedLength = rs.getInt("shed_lenght");
                int shedWidth = rs.getInt("shed_width");
                int roofAngle = rs.getInt("roof_angle");
                String date = rs.getString("o_date");
                int emplID = rs.getInt("userid");

                Order o = new Order(id, emplID, height, width, length, shedLength, shedWidth, roofAngle, date);
                orders.add(o);
            }

        } catch (SQLException e) {
            //Do something
        }
        return orders;
    }

    @Override
    public Order getOrder(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void assignOrder(User user, Order order) {
        try {
            Connector c = new Connector();
            Connection con = c.getConnection();
            String query = "UPDATE c_order SET userid = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
                        
            ps.setInt(1, user.getId());
            ps.setInt(2, order.getId());
            
            ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(OrderMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
