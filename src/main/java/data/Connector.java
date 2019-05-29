package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector implements ConnectorInterface {

    private static final String URL = "jdbc:mysql://138.68.92.25:3306/carports";
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password123";
    private static Connector single;

    private static Connection singleton;

    public static Connector getInstance() {
        if(single == null) {
            single = new Connector();
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