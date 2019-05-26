/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Customer;
import entity.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Annika
 */
public class UserMapper implements UserInterface {
    private Connection conn;
    
    public UserMapper(ConnectorInterface conn) {
        try {
            this.conn = conn.connect();
        } catch (ClassNotFoundException | SQLException e) {
            
        }
    }

    /*public Employee logIn(String username, String password) throws LogInException {
        Employee user = null;
        try {
            String query = "SELECT username, pass, id FROM c_user WHERE username = ? AND pass = ?";
            PreparedStatement ps = Connector.connection().prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new Employee(rs.getInt("id"), username, password);
            }

        } catch (Exception ex) {
            throw new LogInException();
        }

        return user;
    }*/
    
    /**
     * Attemps to log in an employee with given username and password
     * @param username
     * @param password
     * @return an employee object on successful login
     * @throws LogInException
     */
    @Override
    public Employee logIn(String username, String password) throws LogInException {
        Employee user = null;
        try {
            String query = "SELECT initials, passw, id FROM employee WHERE initials = ? AND passw = ?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new Employee(rs.getInt("id"), username, password);
            } else {
                throw new LogInException();
            }
        } catch (SQLException ex) {
            
        }

        return user;
    }

     /**
     * Gets a list of all employees
     * @return List of employee objects
     */
    @Override
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList();

        try {
            String query = "SELECT * FROM employee";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                employees.add(new Employee(rs.getInt("id"), rs.getString("initials"), rs.getString("passw")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return employees;
    }

    public int createCustomer(Customer customer) {
        int id = 0;

        try {
            String query = "INSERT INTO customer (cname, email, address, zip, phone) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getAddress());
            ps.setInt(4, customer.getZip());
            ps.setInt(5, customer.getPhone());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    /**
     * Gets a specific customer by given id
     * @param customerID
     * @return Customer object
     */
    @Override
    public Customer getCustomer(int customerID) {
        Customer customer = null;

        try {

            String query = "SELECT * FROM customer WHERE id = " + customerID + ";";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customer = new Customer(rs.getInt("id"), rs.getString("cname"), rs.getString("email"), rs.getString("address"), rs.getInt("zip"), rs.getInt("phone"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return customer;
    }

    /**
     * Gets a specific employee by a given ID
     * @param id
     * @return Employee object
     */
    public Employee getEmployee(int emplId) {
        Employee empl = null;
        try {
            String query = "SELECT * FROM employee WHERE id = ?";
            

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, emplId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                empl = new Employee(rs.getInt("id"), rs.getString("initials"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return empl;
    }

}
