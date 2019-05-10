/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Customer;
import entity.Order;
import entity.Employee;
import entity.Odetail;
import entity.Product;
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

    private ResultSet getResult(String query, int... id) throws SQLException, ClassNotFoundException {
        ResultSet rs = null;

        Connection con = Connector.connection();
        PreparedStatement ps = con.prepareStatement(query);
        
        for(int i = 0; i < id.length; i++) {
            ps.setInt(i+1, id[i]);
        }
        
        rs = ps.executeQuery();

        return rs;
    }

    private List<Order> order(ResultSet rs) throws SQLException {
        List<Order> orders = new ArrayList<>();
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
            Employee e = null;

            if (emplID != 0) {
                e = getEmployee(emplID);
            }

            Order o = new Order(id, e, height, width, length, shedLength, shedWidth, roofAngle, date, status, salesPrice, custId);
            orders.add(o);
        }
        return orders;
    }

    private Employee getEmployee(int id) {
        UserMapper u = new UserMapper();
        return u.getEmployee(id);
    }

    //Might not be used
    @Override
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            String query = "SELECT * FROM c_order";
            ResultSet rs = getResult(query);
            orders = order(rs);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orders;

    }

    @Override
    public List<Order> getOrdersUnassigned() {
        List<Order> orders = new ArrayList<>();
        try {
            String query = "SELECT * FROM c_order WHERE emp_id IS NULL";
            ResultSet rs = getResult(query);
            orders = order(rs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> getUnfinishedOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            String query = "SELECT * FROM c_order WHERE o_status != 'delivered'";
            ResultSet rs = getResult(query);
            orders = order(rs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    @Override
    public List<Order> getOwnOrders(int emplId) {
        List<Order> orders = new ArrayList<>();
        try {
            String query = "SELECT * FROM c_order WHERE emp_id = ?";
            ResultSet rs = getResult(query, emplId);
            orders = order(rs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order getOrder(int id) {
        Order o = null;
        try {
            String query = "SELECT * FROM c_order WHERE id = ?";
            ResultSet rs = getResult(query, id);
            o = order(rs).get(0);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
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
            Logger.getLogger(OrderMapper.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void assignOrder(int orderID, int employeeID) {
        try {
            Connection con = Connector.connection();
            String query = "UPDATE c_order SET emp_id = ? WHERE id = ?";
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
    
    public List<Product> getCarportList(Order order) {
        List<Product> products = new ArrayList();
        
        // stolper
        
        return products;
        
 }
    @Override
    public void createOdetail(List<Odetail> odetails) {
        try {
            Connection con = Connector.connection();
            con.setAutoCommit(false);
            String query = "INSERT INTO `odetails` (prod_id, order_id, amount) "
                    + "VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            
            for (Odetail od : odetails) {
                ps.setInt(1, od.getProduct().getId());
                ps.setInt(2, od.getOrder_id());
                ps.setInt(3, od.getQty());  
                ps.executeUpdate();
            }
            
            con.commit();
            con.setAutoCommit(true);
            
        } catch (SQLException | ClassNotFoundException e) {
 
            try {
                Connection con = Connector.connection();
                con.rollback();
            } catch (SQLException | ClassNotFoundException e1) {
                System.out.println("Could not rollback updates");
            }
            
        }

    }

    @Override
    public void editOdetails(int orderID, List<Odetail> details) {
       try {
            Connection con = Connector.connection();
            con.setAutoCommit(false);
            String query = "UPDATE `odetails` SET prod_id = ?, amount = ? WHERE order_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            
            for (Odetail od : details) {
                ps.setInt(1, od.getProduct().getId());
                ps.setInt(2, od.getQty());  
                ps.executeUpdate();
            }
            
            con.commit();
            con.setAutoCommit(true);
            
        } catch (SQLException | ClassNotFoundException e) {
 
            try {
                Connection con = Connector.connection();
                con.rollback();
            } catch (SQLException | ClassNotFoundException e1) {
                System.out.println("Could not rollback updates");
            }
            
        } 
    }
    
}
