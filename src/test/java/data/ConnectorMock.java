/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Annika
 */
public class ConnectorMock implements ConnectorInterface {

    private static final String URL = "jdbc:mysql://138.68.92.25:3306/test_carports";
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password123";
    private static ConnectorMock single;

    private static Connection singleton;

    public static ConnectorMock getInstance() {
        if (single == null) {
            single = new ConnectorMock();
        }

        return single;
    }

    @Override
    public Connection connect() throws ClassNotFoundException, SQLException {
        if (singleton == null || singleton.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            singleton = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        return singleton;
    }

}
