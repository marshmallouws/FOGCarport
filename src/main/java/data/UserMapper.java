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
    
    
}
