package data;

import model.Bill;
import model.Client;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

import static connection.ConnectionBuilder.close;
import static connection.ConnectionBuilder.getConnection;

/**
 * Class that extends {@link AbstractDAO<Bill>}}
 */
public class BillDAO extends AbstractDAO<Bill>{
    public boolean insertIntoDB(Bill instance){
        String q = createInsertQueryBill();
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            Field[] fields = Bill.class.getDeclaredFields();
            connection = getConnection();
            statement = connection.prepareStatement(q);
            PropertyDescriptor pd;
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Integer value = (Integer) fields[i].get(instance);
                statement.setInt(i+1,value);
            }
            if(statement != null) {
                statement.executeUpdate();
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.log(Level.WARNING, Bill.class.getName() + "DAO:insertIntoDB "+ e.getMessage());
        }finally {
            close(statement);
            close(connection);
        }
        return false;
    }
}
