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

    public Employee logIn(String username, String password) throws LogInException {
        
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
    } 

    @Override
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList();

        try {
            String query = "SELECT * FROM c_user";
            PreparedStatement ps = Connector.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                employees.add(new Employee(rs.getInt("id"), rs.getString("username"), rs.getString("pass")));
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        return employees;
    }
    
    public int createCustomer(Customer customer) {
        int id = 0;
        
        try {
            Connection con = Connector.connection();
            String query = "INSERT INTO customer (email, address, zip, phone) "
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getEmail());
            ps.setString(2, customer.getAddress());
            ps.setInt(3, customer.getZip());
            ps.setInt(4, customer.getPhone());
            
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()) {
                id = rs.getInt(1);
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            
        }
        
        return id;
    }

}
