package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.model.CartItem;
import com.fashionstore.util.DBConnection;

public class CartItemDAOImpl implements CartItemDAO {

    private static final String INSERT_CART_ITEM = """
        INSERT INTO cart_items (cart_id, variant_id, quantity)
        VALUES (?, ?, ?)
    """;

    private static final String CHECK_ITEM_EXISTS = """
        SELECT cart_item_id, quantity
        FROM cart_items
        WHERE cart_id = ? AND variant_id = ?
    """;

    private static final String UPDATE_CART_ITEM = """
        UPDATE cart_items
        SET quantity = ?
        WHERE cart_item_id = ?
    """;

    private static final String DELETE_CART_ITEM = """
        DELETE FROM cart_items
        WHERE cart_item_id = ?
    """;

    // ✅ FINAL FIXED QUERY
    private static final String SELECT_CART_ITEMS = """
    	    SELECT ci.cart_item_id, ci.cart_id, ci.variant_id, ci.quantity,
    	           p.product_id, p.product_name, p.brand, p.price, p.image_url,
    	           v.size
    	    FROM cart_items ci
    	    JOIN product_variants v ON ci.variant_id = v.variant_id
    	    JOIN products p ON v.product_id = p.product_id
    	    WHERE ci.cart_id = ?
    	""";
    
    @Override
    public boolean addCartItem(CartItem cartItem) {

        try (Connection con = DBConnection.getConnection()) {

            try (PreparedStatement checkStmt = con.prepareStatement(CHECK_ITEM_EXISTS)) {
                checkStmt.setInt(1, cartItem.getCartId());
                checkStmt.setInt(2, cartItem.getVariantId());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int cartItemId = rs.getInt("cart_item_id");
                        int existingQty = rs.getInt("quantity");

                        try (PreparedStatement updateStmt = con.prepareStatement(UPDATE_CART_ITEM)) {
                            updateStmt.setInt(1, existingQty + cartItem.getQuantity());
                            updateStmt.setInt(2, cartItemId);
                            return updateStmt.executeUpdate() > 0;
                        }

                    } else {
                        try (PreparedStatement insertStmt = con.prepareStatement(INSERT_CART_ITEM)) {
                            insertStmt.setInt(1, cartItem.getCartId());
                            insertStmt.setInt(2, cartItem.getVariantId());
                            insertStmt.setInt(3, cartItem.getQuantity());
                            return insertStmt.executeUpdate() > 0;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateCartItemQuantity(int cartItemId, int quantity) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_CART_ITEM)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, cartItemId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean removeCartItem(int cartItemId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_CART_ITEM)) {

            stmt.setInt(1, cartItemId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<CartItem> getCartItems(int cartId) {

        List<CartItem> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_CART_ITEMS)) {

            stmt.setInt(1, cartId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setCartItemId(rs.getInt("cart_item_id"));
                    item.setCartId(rs.getInt("cart_id"));
                    item.setVariantId(rs.getInt("variant_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setBrand(rs.getString("brand"));
                    item.setPrice(rs.getBigDecimal("price"));
                    item.setImageUrl(rs.getString("image_url"));
                    item.setSize(rs.getString("size"));
                    list.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean updateCartItemQuantityByCartAndVariant(int cartId, int variantId, int quantity) {
        return false;
    }

    @Override
    public java.math.BigDecimal getCartTotal(int cartId) {
        return java.math.BigDecimal.ZERO;
    }
}