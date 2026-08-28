package com.fashionstore.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver Loaded");

            // 1. Check if properties exist in db.properties file (ignored by Git)
            Properties props = new Properties();
            try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (in != null) {
                    props.load(in);
                }
            } catch (Exception e) {
                System.out.println("ℹ️ No db.properties found on classpath, checking environment variables...");
            }

            // 2. Read from Environment Variables (for Cloud deployment) or fallback to db.properties
            String envUrl = System.getenv("DB_URL");
            if (envUrl == null) envUrl = System.getenv("MYSQL_URL");
            if (envUrl == null) envUrl = System.getenv("DATABASE_URL");

            String envUser = System.getenv("DB_USER");
            if (envUser == null) envUser = System.getenv("MYSQLUSER");

            String envPassword = System.getenv("DB_PASSWORD");
            if (envPassword == null) envPassword = System.getenv("MYSQLPASSWORD");

            url = (envUrl != null && !envUrl.trim().isEmpty()) 
                    ? envUrl 
                    : props.getProperty("db.url", "jdbc:mysql://127.0.0.1:3306/fashion_store?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata");

            user = (envUser != null && !envUser.trim().isEmpty()) 
                    ? envUser 
                    : props.getProperty("db.user", "root");

            password = (envPassword != null) 
                    ? envPassword 
                    : props.getProperty("db.password", "");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ ERROR: Failed to connect to MySQL database!");
            System.err.println("❌ URL: " + url);
            System.err.println("❌ ERROR MESSAGE: " + e.getMessage());
            System.err.println("❌ SQL STATE: " + e.getSQLState());
            e.printStackTrace();
            throw new RuntimeException("Database connection failed: " + e.getMessage(), e);
        }
    }
}