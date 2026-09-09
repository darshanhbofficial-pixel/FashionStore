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
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver Loaded");

            // Load local db.properties if available
            Properties props = new Properties();

            try (InputStream in = DBConnection.class.getClassLoader()
                    .getResourceAsStream("db.properties")) {

                if (in != null) {
                    props.load(in);
                }

            } catch (Exception e) {
                System.out.println("No db.properties found.");
            }

            // Railway / Cloud environment variables
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String envUser = System.getenv("MYSQLUSER");
            String envPassword = System.getenv("MYSQLPASSWORD");

            // CLOUD DEPLOYMENT
            if (host != null && !host.isBlank()) {

                // Your imported Fashion Store schema
                String database = "fashion_store";

                url = "jdbc:mysql://" + host + ":" + port + "/" + database
                        + "?useSSL=true"
                        + "&serverTimezone=Asia/Kolkata"
                        + "&allowPublicKeyRetrieval=true";

                user = envUser;
                password = envPassword;

                System.out.println("Using Railway Cloud Database");
                System.out.println("Database: " + database);

            } else {

                // LOCAL DEVELOPMENT
                url = props.getProperty(
                        "db.url",
                        "jdbc:mysql://127.0.0.1:3306/fashion_store"
                                + "?useSSL=false"
                                + "&allowPublicKeyRetrieval=true"
                                + "&serverTimezone=Asia/Kolkata");

                user = props.getProperty("db.user", "root");
                password = props.getProperty("db.password", "");

                System.out.println("Using Local Database");
            }

        } catch (ClassNotFoundException e) {

            System.err.println("MySQL Driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {

        try {
            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {

            System.err.println("ERROR: Failed to connect to MySQL database!");
            System.err.println("ERROR MESSAGE: " + e.getMessage());
            System.err.println("SQL STATE: " + e.getSQLState());

            e.printStackTrace();

            throw new RuntimeException(
                    "Database connection failed: " + e.getMessage(),
                    e);
        }
    }
}