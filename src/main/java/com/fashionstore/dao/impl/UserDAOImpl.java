package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.model.User;
import com.fashionstore.util.DBConnection;

public class UserDAOImpl implements UserDAO {

    // ================= SQL QUERIES =================

    private static final String INSERT_USER_SQL = """
        INSERT INTO users (
            full_name, email, phone, password,
            address_line1, address_line2, city, state, pincode, country
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    private static final String LOGIN_USER_SQL = """
        SELECT * FROM users
        WHERE email = ? AND password = ?
        """;

    private static final String GET_USER_BY_ID_SQL = """
        SELECT * FROM users WHERE user_id = ?
        """;

    private static final String GET_USER_BY_EMAIL_SQL = """
        SELECT * FROM users WHERE email = ?
        """;

    private static final String GET_USER_BY_PHONE_SQL = """
        SELECT * FROM users WHERE phone = ?
        """;

    private static final String EMAIL_EXISTS_SQL = """
        SELECT 1 FROM users WHERE email = ?
        """;

    private static final String PHONE_EXISTS_SQL = """
        SELECT 1 FROM users WHERE phone = ?
        """;

    private static final String GET_ALL_USERS_SQL = """
        SELECT * FROM users
        """;

    private static final String UPDATE_USER_SQL = """
        UPDATE users SET
            full_name = ?, phone = ?, address_line1 = ?, address_line2 = ?,
            city = ?, state = ?, pincode = ?, country = ?
        WHERE user_id = ?
        """;

    private static final String UPDATE_PASSWORD_SQL = """
        UPDATE users SET password = ? WHERE user_id = ?
        """;

    // ================= METHODS =================

    @Override
    public boolean registerUser(User user) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_USER_SQL)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getAddressLine1());
            ps.setString(6, user.getAddressLine2());
            ps.setString(7, user.getCity());
            ps.setString(8, user.getState());
            ps.setString(9, user.getPincode());
            ps.setString(10, user.getCountry());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public User loginUser(String email, String password) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_USER_BY_EMAIL_SQL)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        return extractUser(rs);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public User getUserById(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_USER_BY_ID_SQL)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUser(rs);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_USER_BY_EMAIL_SQL)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUser(rs);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public User getUserByPhone(String phone) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_USER_BY_PHONE_SQL)) {

            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUser(rs);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean emailExists(String email) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(EMAIL_EXISTS_SQL)) {

            ps.setString(1, email);
            return ps.executeQuery().next();

        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean phoneExists(String phone) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(PHONE_EXISTS_SQL)) {

            ps.setString(1, phone);
            return ps.executeQuery().next();

        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL_USERS_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractUser(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public boolean updateUser(User user) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_USER_SQL)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getAddressLine1());
            ps.setString(4, user.getAddressLine2());
            ps.setString(5, user.getCity());
            ps.setString(6, user.getState());
            ps.setString(7, user.getPincode());
            ps.setString(8, user.getCountry());
            ps.setInt(9, user.getUserId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_PASSWORD_SQL)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Database error in UserDAO: " + e.getMessage(), e);
        }
    }

    // ================= HELPER =================

    private User extractUser(ResultSet rs) throws Exception {
        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setPassword(rs.getString("password"));

        user.setAddressLine1(rs.getString("address_line1"));
        user.setAddressLine2(rs.getString("address_line2"));
        user.setCity(rs.getString("city"));
        user.setState(rs.getString("state"));
        user.setPincode(rs.getString("pincode"));
        user.setCountry(rs.getString("country"));
        
        // 🔥 EXTRACT ROLE
        try {
            user.setRole(rs.getString("role"));
        } catch (Exception e) {
            user.setRole("USER"); // Fallback if column missing
        }

        return user;
    }
    @Override
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}