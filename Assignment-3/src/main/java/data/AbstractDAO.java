package data;
import model.validators.NumericalValidator;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static connection.ConnectionBuilder.*;

/**
 * Generic class used for calling database related operations (CRUD).
 * @param <T> the generic type
 * {@link model.Client};
 * {@link model.Product};
 * {@link model.Orders};
 * {@link model.Bill};
 */

public class AbstractDAO<T> {
    private final Class<T> type;
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Builds the selection query
     * @param field the particular field used for filtering the results
     * @return the Select query
     */
    public String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT");
        sb.append(" * ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ").append(field).append(" =?");
        return sb.toString();
    }

    public ArrayList<String> getTableHeader() {
        ArrayList<String> header = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();
        for (Field f : fields) {
            header.add(f.getName());
        }
        return header;
    }

    /**
     * Generic method used for retrieving all the entries of a table
     * @return a list containing all the entries
     */
    public List<T> findAll() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        String q = "SELECT * FROM " + type.getSimpleName().toLowerCase();
        try {
            connection = getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(q);
            return createObjects(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            close(rs);
            close(statement);
            close(connection);
        }
        return null;
    }

    /**
     * Generic method used for retrieving an entry of type T from the database by its id
     * @param id the id of the entry
     * @return an object of type T
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = createSelectQuery(type.getSimpleName() + "_id");
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();

            return createObjects(rs).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            close(rs);
            close(statement);
            close(connection);
        }
        return null;
    }

    /**
     * Generic method used for inserting an entry inside the database.
     * The fields of type T are iterated in order to check the primitive type used for creating the PreparedStatement.
     * @param data a vector of Strings representing the data inserted inside the GUI.
     * @return bool value representing whether the operation completed successfully or not
     */
    public boolean insertIntoDB(String[] data) {
        String q = createInsertQuery();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Field[] fields = type.getDeclaredFields();
            connection = getConnection();
            statement = connection.prepareStatement(q);
            for (int i = 1; i < fields.length && statement != null; i++) {
                switch (fields[i].getType().getSimpleName()) {
                    case "String" -> {
                        statement.setString(i, data[i - 1]);

                    }
                    case "int" -> {
                        if (NumericalValidator.containsOnlyNumbers(data[i - 1])) {
                            statement.setInt(i, Integer.parseInt(data[i - 1]));
                        } else {
                            return false;
                        }
                    }
                }
            }
            if (statement != null) {
                statement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insertIntoDB " + e.getMessage());
        } finally {
            close(statement);
            close(connection);
        }
        return false;
    }

    /**
     * Generic method used for deleting an entry from the database.
     * @param instance an entry of type T needed for its id
     * @return bool value representing whether the operation completed successfully or not
     */

    public boolean deleteFromDB(T instance) {
        Connection connection = null;
        PreparedStatement statement = null;
        int id = getIdOfInstance(instance);
        if (id != -1) {
            String q = createDeleteQueryById();
            q += String.valueOf(id);
            try {
                connection = getConnection();
                statement = connection.prepareStatement(q);
                statement.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.log(Level.WARNING, type.getName() + "DAO:deleteFromDB " + e.getMessage());
            } finally {
                close(statement);
                close(connection);
            }
        }
        return false;
    }

    /**
     * Generic method used for updating an existing entry.
     * @param id the id of the instance which is to be updated
     * @param fields the fields that are to be changed
     * @param values the changes that have to be made
     * @return  bool value representing whether the operation completed successfully or not
     */
    public boolean update(int id, String[] fields, String[] values) {
        Connection connection = null;
        PreparedStatement statement = null;
        T instance = findById(id);
        String q = createUpdateQuery(fields);
        try {
            connection = getConnection();
            statement = connection.prepareStatement(q);
            constructUpdate(instance, statement, fields, values);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            close(statement);
            close(connection);
        }
        return false;
    }

    /**
     * Retrieve the id of an instance of type t
     * @param instance
     * @return the if of the instance
     */
    public int getIdOfInstance(T instance)  {
        int id = -1;
        Field[] fields = instance.getClass().getDeclaredFields();
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fields[0].getName(), instance.getClass());
            id = (int) pd.getReadMethod().invoke(instance);
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * Build the update query
     * @param givenFields the fields which will be changed
     * @return the string representing the incomplete query statement
     */
    private String createUpdateQuery(String[] givenFields) {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append(" ").append("SET");
        for (int i = 0; i < givenFields.length; i++) {
            if (i == givenFields.length - 1) {
                sb.append(" ").append(givenFields[i]).append("=?");
            } else {
                sb.append(" ").append(givenFields[i]).append("=?").append(",");
            }
        }
        sb.append(" where ").append(type.getSimpleName().toLowerCase()).append("_").append("id=?");

        return sb.toString();
    }

    /**
     * Insert the values inside the PreparedStatement used for updating an entry
     * @param instance the instance to be updated
     * @param statement the statement used for executing the update
     * @param givenFields the fields to be modified
     * @param values the values that have to be inserted
     */
    private void constructUpdate(T instance, PreparedStatement statement, String[] givenFields, String[] values) throws SQLException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        PropertyDescriptor pd = null;
        int index = 1;
        for (String s : givenFields) {
            for (Field field : fields) {
                if (s.equals(field.getName())) {
                    String primitive = field.getType().getSimpleName();
                    switch (primitive) {
                        case "String" -> {
                            statement.setString(index, values[index - 1]);
                            index++;
                        }
                        case "int", "Integer" -> {
                            statement.setInt(index, Integer.parseInt(values[index - 1]));
                            index++;
                        }
                        default -> {
                        }
                    }
                }
            }
        }
        pd = new PropertyDescriptor(type.getSimpleName() + "_id", instance.getClass());
        statement.setInt(givenFields.length + 1, (Integer) pd.getReadMethod().invoke(instance));
    }

    /**
     * Build the insert query
     * @return a string representing the query
     */
    private String createInsertQuery() {
        Field[] fields = type.getDeclaredFields();
        StringBuilder query = new StringBuilder("INSERT INTO " + type.getSimpleName().toLowerCase());
        query.append("(");
        for (int i = 1; i < fields.length; i++) {
            query.append(" ");
            query.append(fields[i].getName());
            if (i != fields.length - 1) {
                query.append(",");
            }
        }
        query.append(") VALUES (");
        for (int i = 1; i < fields.length; i++) {
            if (i != fields.length - 1) {
                query.append("?,");
            } else {
                query.append("?)");
            }
        }
        return query.toString();
    }

    /**
     * Build the insert query for the Bill record
     * @return a string representing the query
     */
    protected String createInsertQueryBill() {
        Field[] fields = type.getDeclaredFields();
        StringBuilder query = new StringBuilder("INSERT INTO " + type.getSimpleName().toLowerCase());
        query.append("(");
        for (int i = 0; i < fields.length; i++) {
            query.append(" ");
            query.append(fields[i].getName());
            if (i != fields.length - 1) {
                query.append(",");
            }
        }
        query.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            if (i != fields.length - 1) {
                query.append("?,");
            } else {
                query.append("?)");
            }
        }
        return query.toString();
    }

    /**
     * Build a deletion query filtered by the id
     * @return a string representing the query
     */
    private String createDeleteQueryById() {
        StringBuilder query = new StringBuilder("DELETE FROM " + type.getSimpleName().toLowerCase());
        query.append(" WHERE ");
        query.append(type.getSimpleName().toLowerCase()).append("_").append("id=");
        return query.toString();
    }

    /**
     * Generic method used for creating a list of objects of type T.
     * It creates an instance of type T by looking for the default constructor which has to be empty.
     * Call the setter methods for each entry of the database, using PropertyDescriptor.
     * Add each instance created inside a list and then return the list
     * @param resultSet the resultSet resulting from a query
     * @return a list of objects of type T
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (Constructor constructor : ctors) {
            ctor = constructor;
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                assert ctor != null;
                ctor.setAccessible(true);
                Object instance = ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add((T) instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | SQLException |
                 IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Class<T> getType() {
        return type;
    }
}
