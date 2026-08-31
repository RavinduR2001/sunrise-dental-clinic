package com.sunrisedental.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static Properties props = new Properties();

    static {
        try {
            InputStream input = DatabaseConnection.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

            if (input == null) {
                System.err.println("db.properties file not found! Using default values.");
                props.setProperty("db.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
                props.setProperty("db.url", "jdbc:sqlserver://localhost:1433;databaseName=sunrise_dental;encrypt=true;trustServerCertificate=true;integratedSecurity=true");
            } else {
                props.load(input);
                input.close();
            }

            Class.forName(props.getProperty("db.driver"));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database driver: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database properties: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url");
        // No username/password needed for Windows Authentication
        return DriverManager.getConnection(url);
    }

    public static Connection getNewConnection() throws SQLException {
        return getConnection();
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}