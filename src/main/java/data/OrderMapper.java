/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Category;
import entity.Customer;
import entity.Order;
import entity.Employee;
import entity.Odetail;
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

/**
 *
 * @author Bitten
 */
public class OrderMapper implements OrderInterface {

    @Override
    public int createOrder(Order order, Customer customer) {
        try {
            Connection con = Connector.connection();
            String SQL = "INSERT INTO `c_order` (height, length, width, shed_length, shed_width, roof_angle, roof_type, cust_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getHeight());
            ps.setInt(2, order.getLenght());
            ps.setInt(3, order.getWidth());
            ps.setInt(4, order.getShedLength());
            ps.setInt(5, order.getShedWidth());
            ps.setInt(6, order.getRoofAngle());
            ps.setInt(7, order.getRoofType());
            ps.setInt(8, customer.getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private ResultSet getResult(String query, int... id) throws SQLException, ClassNotFoundException {
        ResultSet rs = null;

        Connection con = Connector.connection();
        PreparedStatement ps = con.prepareStatement(query);

        for (int i = 0; i < id.length; i++) {
            ps.setInt(i + 1, id[i]);
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
            int roofType = rs.getInt("roof_type");
            String date = rs.getString("o_date");
            int emplID = rs.getInt("emp_id");
            String status = rs.getString("o_status");
            double salesPrice = rs.getDouble("sales_price");
            int custId = rs.getInt("cust_id");
            Employee e = null;

            if (emplID != 0) {
                e = getEmployee(emplID);
            }

            Order o = new Order(id, e, height, width, length, roofType, shedLength, shedWidth, roofAngle, date, status, salesPrice, custId);
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
            String query = "INSERT INTO `odetail` (prod_id, order_id, qty, amount, cmt) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            for (Odetail od : odetails) {
                ps.setInt(1, od.getProduct().getVariant_id());
                ps.setInt(2, od.getOrder_id());
                ps.setInt(3, od.getQty());
                ps.setDouble(4, od.getAmount());
                ps.setString(5, od.getComment());
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            try {
                Connection con = Connector.connection();
                con.rollback();
            } catch (SQLException | ClassNotFoundException e1) {
                System.out.println("Could not rollback updates");
                e1.printStackTrace();
            }

        }

    }

    @Override
    public void editOdetails(List<Odetail> details) {
        try {
            Connection con = Connector.connection();
            con.setAutoCommit(false);
            //String query = "UPDATE `odetails` SET prod_id = ?, amount = ? WHERE order_id = ?";
            String query = "UPDATE odetail SET cmt = ? WHERE id = ?;";
            PreparedStatement ps = con.prepareStatement(query);

            for (Odetail od : details) {
//                ps.setInt(1, od.getProduct().getVariant_id());
//                ps.setInt(2, od.getQty());
                ps.setString(1, od.getComment());
                ps.setInt(2, od.getId());
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
    public Category getCategory(int prod_id) {
        Category cat = null;

        try {

            Connection con = Connector.connection();
            String query = "SELECT categories.id, categories.category_name FROM categories JOIN products_in_categories ON products_in_categories.category_id = categories.id WHERE products_in_categories.product_id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, prod_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cat = new Category(rs.getInt(1), rs.getString(2));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return cat;
    }

    @Override
    public Product getProduct(int prod_id) {

        Product prod = null;
        Category category = null;

        try {

            Connection con = Connector.connection();
            String query = "SELECT product_variants.id, product_variants.product_id, product_variants.length, product_variants.price, product_variants.stock, products.product_name, products.thickness, products.width "
                    + "FROM product_variants "
                    + "JOIN products ON product_variants.product_id = products.id "
                    + "WHERE product_variants.id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, prod_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int prodID = rs.getInt("product_id");

                category = getCategory(prodID);
                prod = new Product(rs.getInt("product_id"), rs.getInt("id"), category, rs.getInt("thickness"), rs.getInt("length"), rs.getInt("width"), rs.getInt("price"), true, rs.getInt("stock"), rs.getString("product_name"));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return prod;
    }

    @Override
    public List<Odetail> getOdetails(int orderID) {
        List<Odetail> details = new ArrayList<>();
        Product prod = null;
        try {
            Connection con = Connector.connection();
            String query = "SELECT * FROM odetail WHERE order_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                prod = getProduct(rs.getInt("prod_id"));

                details.add(new Odetail(rs.getInt("id"), prod, rs.getInt("order_id"), rs.getInt("qty"), rs.getDouble("amount"), rs.getString("cmt")));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return details;
    }

}
