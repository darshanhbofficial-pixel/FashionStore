package com.fashionstore.util;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            if (connection != null && !connection.isClosed()) {
                System.out.println(" Database connection SUCCESSFUL!");
            } else {
                System.out.println(" Connection returned NULL");
            }

        } catch (Exception e) {
            System.out.println("Database connection FAILED!");
            e.printStackTrace();
        }
    }
}