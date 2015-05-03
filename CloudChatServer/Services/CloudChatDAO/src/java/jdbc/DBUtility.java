package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtility {

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                connection = DriverManager.getConnection(DBConfig.DB_HOST, DBConfig.DB_USER, DBConfig.DB_PASS);
            } catch (SQLException err) {
                System.out.println(err.getMessage());
            }
            return connection;
        }
    }
}
