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

    @Override
    public void logIn(User user) throws LogInException {
        try {
            Connector c = new Connector();
            Connection con = c.getConnection();
            
            String query = "SELECT username, password FROM c_user WHERE usernamem = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            
            ResultSet rs = ps.executeQuery();
            
            if(!rs.next()) {
                throw new LogInException(); // Needs to be caught when user is trying to log in.
            }
            
        } catch (SQLException ex) {
            // Do something
        }
    }
    
    
}
