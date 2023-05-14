package connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection Builder for creating a static instance of the database connection
 */

public class ConnectionBuilder {
    private static final Logger LOGGER = Logger.getLogger(ConnectionBuilder.class.getName());
    private static final String DRIVER =  "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/product_order_a3";
    private static final  String USER = "admin";
    private static final String PASS = "proiectbd";

    private static ConnectionBuilder dbInstance = new ConnectionBuilder();

    private ConnectionBuilder(){
        try{
            Class.forName(DRIVER);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private Connection createConnection(){
        Connection connection=null;
        try{
            connection = DriverManager.getConnection(URL, USER, PASS);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getConnection(){
        return dbInstance.createConnection();
    }

    public static void close(Connection connection){
        if(connection != null){
            try{
                connection.close();
            }catch(SQLException e){
                LOGGER.log(Level.WARNING, "An error occured while trying to close the database");
            }
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
            }
        }
    }
}
