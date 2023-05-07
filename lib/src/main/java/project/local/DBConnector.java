/**
 * This class is responsible for establishing, managing, and closing the database connections and query execution operations.
 * 
 * @author Vatsal Thakkar
 */

package project.local;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DBConnector implements AutoCloseable {

    private String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private String DB_PASS = System.getenv().getOrDefault("DB_PASS", "");/*
                                                                                     * set this according to your
                                                                                     * password for root user
                                                                                     */
    private String DB_HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private String DB_PORT = System.getenv().getOrDefault("DB_PORT", "3306");
    private String DB_NAME = System.getenv().getOrDefault("DB_NAME", "pkl");

    private Connection dbConnection;

    /**
     * Establish database connection using credentials from the environment
     * 
     * 
     * @author Vatsal Thakkar
     * 
     * @return (dbConnection) : DB Connection Reference
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {

        if (this.dbConnection == null) {
            String dbUri = String.format("jdbc:mysql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);
            this.dbConnection = DriverManager.getConnection(dbUri, DB_USER, DB_PASS);
        }

        return this.dbConnection;
    }

    /**
     * 
     * 
     * @author Vatsal Thakkar
     * 
     * @param query: Valid SQL query
     * 
     * @return (response): Reference to ResultSet
     * @throws SQLException
     */
    public ResultSet execute(String query) throws SQLException {
        Connection dbConnection = getConnection();
        Objects.requireNonNull(dbConnection);

        PreparedStatement sqlStatement = dbConnection.prepareStatement(query);
        ResultSet response = sqlStatement.executeQuery();
        return response;
    }

    /**
     * Overridden to auto close the dbconnection in case of failures in execution.
     * 
     * @author Vatsal Thakkar
     */
    @Override
    public void close() throws SQLException {
        if (!this.dbConnection.isClosed()) {
            this.dbConnection.close();
        }
    }
}
