package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.model.Cart;
import com.fashionstore.util.DBConnection;

public class CartDAOImpl implements CartDAO {

    // ================= SQL QUERIES =================

    private static final String INSERT_CART_SQL = """
        INSERT INTO cart (user_id)
        VALUES (?)
        """;

    private static final String GET_CART_BY_ID_SQL = """
        SELECT * FROM cart WHERE cart_id = ?
        """;

    private static final String GET_CART_BY_USER_SQL = """
        SELECT * FROM cart WHERE user_id = ?
        """;

    private static final String DELETE_CART_SQL = """
        DELETE FROM cart WHERE cart_id = ?
        """;

    private static final String CART_EXISTS_SQL = """
        SELECT 1 FROM cart WHERE user_id = ?
        """;

    // ================= METHODS =================

    @Override
    public boolean createCart(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_CART_SQL)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Cart getCartById(int cartId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_CART_BY_ID_SQL)) {

            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCart(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Cart getCartByUserId(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_CART_BY_USER_SQL)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCart(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Cart getOrCreateCartByUserId(int userId) {
        Cart cart = getCartByUserId(userId);

        if (cart != null) {
            return cart;
        }

        // Create new cart
        boolean created = createCart(userId);

        if (created) {
            return getCartByUserId(userId);
        }

        return null;
    }

    @Override
    public boolean deleteCart(int cartId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_CART_SQL)) {

            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cartExistsByUserId(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(CART_EXISTS_SQL)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= HELPER =================

    private Cart extractCart(ResultSet rs) throws Exception {
        Cart cart = new Cart();

        cart.setCartId(rs.getInt("cart_id"));
        cart.setUserId(rs.getInt("user_id"));

        return cart;
    }
}