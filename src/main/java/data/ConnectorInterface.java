/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Annika
 */
public interface ConnectorInterface {
    public Connection connect() throws ClassNotFoundException, SQLException;
    
}
