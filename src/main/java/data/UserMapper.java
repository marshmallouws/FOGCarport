/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.User;
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
 * @author Annika
 */
public class UserMapper implements UserInterface {

    public User logIn(String username, String password) throws LogInException {
        
        User user = null;
        try {
            String query = "SELECT username, pass, id FROM c_user WHERE username = ? AND pass = ?";
            PreparedStatement ps = Connector.connection().prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User(rs.getInt("id"), username, password);
            }

        } catch (Exception ex) {
            throw new LogInException();
        }

        return user; 
    } 

    @Override
    public List<User> getEmployees() {
        List<User> employees = new ArrayList();

        try {
            String query = "SELECT * FROM c_user";
            PreparedStatement ps = Connector.connection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                employees.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("pass")));
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        return employees;
    }

}
