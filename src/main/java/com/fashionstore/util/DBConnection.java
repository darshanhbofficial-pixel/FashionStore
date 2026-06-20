package com.fashionstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/fashion_store?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata";
    private static final String USER = "root";
    private static final String PASSWORD = "8299";

    // 🔥 DRIVER LOAD DEBUG
    static {
        try { 
            System.out.println("🔄 Loading MySQL Driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver Loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found!");
            e.printStackTrace();
        }
    }

    // 🔥 CONNECTION DEBUG
    public static Connection getConnection() {
        try {
            System.out.println("🔄 Trying DB connection to: " + URL);
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ DB Connected Successfully");
            return connection;

        } catch (SQLException e) {
            System.err.println("❌ ERROR: Failed to connect to MySQL database!");
            System.err.println("❌ URL: " + URL);
            System.err.println("❌ ERROR MESSAGE: " + e.getMessage());
            System.err.println("❌ SQL STATE: " + e.getSQLState());
            System.err.println("❌ ERROR CODE: " + e.getErrorCode());
            
            // Helpful advice
            if (e.getSQLState().startsWith("08")) {
                System.err.println("👉 TIP: This looks like a network error. Is your MySQL server (XAMPP/WorkBench) RUNNING?");
            }
            
            e.printStackTrace();
            throw new RuntimeException("Database connection failed: " + e.getMessage(), e);
        }
    }
}