package com.project.artconnect.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import com.project.artconnect.config.DatabaseConfig;

/**
 * Utility class to manage JDBC connections.
 * 
 */
public class ConnectionManager {

    /**
     * Provides a connection to the MySQL database.
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        
        // return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER,
        // DatabaseConfig.PASSWORD);
         return DriverManager.getConnection(
            DatabaseConfig.URL, 
            DatabaseConfig.USER, 
            DatabaseConfig.PASSWORD
        );
    }
}

