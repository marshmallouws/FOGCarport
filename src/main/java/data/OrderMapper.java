/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Customer;
import entity.Order;
import entity.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bitten
 */
public class OrderMapper implements OrderInterface {

    @Override
    public boolean createOrder(Order order, Customer customer) {
        try {
            Connection con = Connector.connection();
            String SQL = "INSERT INTO `c_order` (height, length, width, shed_length, shed_width, roof_angle, cust_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setInt(1, order.getHeight());
            ps.setInt(2, order.getLenght());
            ps.setInt(3, order.getWidth());
            ps.setInt(4, order.getShedLength());
            ps.setInt(5, order.getShedWidth());
            ps.setInt(6, order.getRoofAngle());
            ps.setInt(7, customer.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM c_order";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                int shedLength = rs.getInt("shed_length");
                int shedWidth = rs.getInt("shed_width");
                int roofAngle = rs.getInt("roof_angle");
                String date = rs.getString("o_date");
                int emplID = rs.getInt("emp_id");
                String status = rs.getString("o_status");
                double salesPrice = rs.getDouble("sales_price");
                int custId = rs.getInt("cust_id");

                Order o = new Order(id, emplID, height, width, length, shedLength, shedWidth, roofAngle, date, status, salesPrice, custId);
                orders.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;

    }

    @Override
    public List<Order> getOrdersUnassigned() {
        List<Order> orders = new ArrayList<>();
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM c_order WHERE emp_id IS NULL";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                int shedLength = rs.getInt("shed_length");
                int shedWidth = rs.getInt("shed_width");
                int roofAngle = rs.getInt("roof_angle");
                String date = rs.getString("o_date");
                int emplID = rs.getInt("emp_id");
                String status = rs.getString("o_status");
                double salesPrice = rs.getDouble("sales_price");
                int custId = rs.getInt("cust_id");

                Order o = new Order(id, emplID, height, width, length, shedLength, shedWidth, roofAngle, date, status, salesPrice, custId);
                orders.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;

    }

    @Override
    public ArrayList<Order> getUnfinishedOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM c_order WHERE o_status != 'delivered'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                int shedLength = rs.getInt("shed_length");
                int shedWidth = rs.getInt("shed_width");
                int roofAngle = rs.getInt("roof_angle");
                String date = rs.getString("o_date");
                int emplID = rs.getInt("emp_id");
                String status = rs.getString("o_status");
                double salesPrice = rs.getDouble("sales_price");
                int custId = rs.getInt("cust_id");

                Order o = new Order(id, emplID, height, width, length, shedLength, shedWidth, roofAngle, date, status, salesPrice, custId);
                orders.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order getOrder(int id) {
        Order o = null;
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM c_order WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int ids = rs.getInt("id");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                int shedLength = rs.getInt("shed_length");
                int shedWidth = rs.getInt("shed_width");
                int roofAngle = rs.getInt("roof_angle");
                String date = rs.getString("o_date");
                int emplID = rs.getInt("emp_id");
                String status = rs.getString("o_status");
                double salesPrice = rs.getDouble("sales_price");
                int custId = rs.getInt("cust_id");

                o = new Order(ids, emplID, height, width, length, shedLength, shedWidth, roofAngle, date, status, salesPrice, custId);
            }

        } catch (Exception e) {
            //Do something
        }
        return o;
    }

    @Override
    public void assignOrder(Employee user, Order order) {
        try {
            Connection con = Connector.connection();
            String query = "UPDATE c_order SET userid = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, user.getId());
            ps.setInt(2, order.getId());

            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(OrderMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void assignOrder(int orderID, int employeeID) {
        try {
            Connection con = Connector.connection();
            String query = "UPDATE c_order SET userid = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, employeeID);
            ps.setInt(2, orderID);

            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Order updateOrder(Order order) throws UpdateException {
        Order o = null;
        String query;
        PreparedStatement ps;
        try {
            Connection con = Connector.connection();
            if (order.employeeId() == 0) {
                query = "UPDATE c_order SET height = ?, length = ?, width = ?, shed_length = ?, shed_width = ?, roof_angle = ? WHERE id = ?;";
                ps = con.prepareStatement(query);

                ps.setInt(1, order.getHeight());
                ps.setInt(2, order.getLenght());
                ps.setInt(3, order.getWidth());
                ps.setInt(4, order.getShedLength());
                ps.setInt(5, order.getShedWidth());
                ps.setInt(6, order.getRoofAngle());
                ps.setInt(7, order.getId());
            } else {
                query = "UPDATE c_order SET height = ?, length = ?, width = ?, shed_length = ?, shed_width = ?, roof_angle = ?, emp_id = ? WHERE id = ?;";
                ps = con.prepareStatement(query);

                ps.setInt(1, order.getHeight());
                ps.setInt(2, order.getLenght());
                ps.setInt(3, order.getWidth());
                ps.setInt(4, order.getShedLength());
                ps.setInt(5, order.getShedWidth());
                ps.setInt(6, order.getRoofAngle());
                ps.setInt(7, order.employeeId());
                ps.setInt(8, order.getId());
                
            }

            if (ps.executeUpdate() == 1) {
                o = getOrder(order.getId());
            } else {
                throw new UpdateException("Fejl ved opdatering af ordre " + order.getId());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            throw new UpdateException("Fejl ved opdatering af ordre " + order.getId());
        }

        return o;
    }
    
    @Override
    public ArrayList<Order> getOldOrders() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
