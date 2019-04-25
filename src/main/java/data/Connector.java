package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {

    private static final String URL = "jdbc:mysql://localhost:3306/carports";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Repsac270390";

    private static Connection singleton;

    public static void setConnection( Connection con ) {
        singleton = con;
    }

    public static Connection connection() throws ClassNotFoundException, SQLException {
        if ( singleton == null || singleton.isClosed() ) {
            Class.forName( "com.mysql.cj.jdbc.Driver" );
            singleton = DriverManager.getConnection( URL, USERNAME, PASSWORD );
        }
        return singleton;
    }
}