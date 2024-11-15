package org.example;

import java.sql.*;

public class DataBaseController {
    private final Connection connection;

    public DataBaseController() throws SQLException {
        connection = getNewConnection();
    }

    private static Connection getNewConnection() throws SQLException {
        String url = "jdbc:h2:mem:test";
        String user = "sa";
        String passwd = "sa";
        return DriverManager.getConnection(url, user, passwd);
    }

    public void doQuery(String customerQuery) throws SQLException {
        if(!connection.isClosed()){
            connection.createStatement().executeUpdate(customerQuery);
        }
    }

    public void dropTable(String tableName) throws SQLException {
        if(!connection.isClosed()){
            String dropTableQuery = "DROP TABLE " + tableName;
            connection.createStatement().executeUpdate(dropTableQuery);
        }
    }

    public ResultSet selectAll(String table) throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM " + table);
    }

    public ResultSet customSelect(String customerQuery) throws SQLException {
        return connection.createStatement().executeQuery(customerQuery);
    }

    public PreparedStatement preparedStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}
