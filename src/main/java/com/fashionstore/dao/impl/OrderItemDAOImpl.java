package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.model.OrderItem;
import com.fashionstore.util.DBConnection;

public class OrderItemDAOImpl implements OrderItemDAO {

    // ================= SQL QUERIES =================

    private static final String INSERT_ORDER_ITEM_SQL = """
        INSERT INTO order_items (order_id, variant_id, quantity, price)
        VALUES (?, ?, ?, ?)
        """;

    private static final String GET_ITEMS_BY_ORDER_SQL = """
        SELECT * FROM order_items WHERE order_id = ?
        """;

    private static final String DELETE_ORDER_ITEM_SQL = """
        DELETE FROM order_items WHERE order_item_id = ?
        """;

    private static final String CLEAR_ORDER_ITEMS_SQL = """
        DELETE FROM order_items WHERE order_id = ?
        """;

    // ================= METHODS =================

    @Override
    public boolean addOrderItem(OrderItem item) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ORDER_ITEM_SQL)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getVariantId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPrice());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean addOrderItems(List<OrderItem> items) {
        boolean success = true;

        for (OrderItem item : items) {
            if (!addOrderItem(item)) {
                success = false;
                break;
            }
        }

        return success;
    }

    @Override
    public List<OrderItem> getItemsByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ITEMS_BY_ORDER_SQL)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractOrderItem(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean deleteOrderItem(int orderItemId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_ORDER_ITEM_SQL)) {

            ps.setInt(1, orderItemId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean clearOrderItems(int orderId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(CLEAR_ORDER_ITEMS_SQL)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= HELPER =================

    private OrderItem extractOrderItem(ResultSet rs) throws Exception {
        OrderItem item = new OrderItem();

        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setVariantId(rs.getInt("variant_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getDouble("price"));

        return item;
    }
}