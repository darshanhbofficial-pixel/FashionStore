package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.model.Order;
import com.fashionstore.util.DBConnection;

public class OrderDAOImpl implements OrderDAO {

    // ================= SQL QUERIES =================

    private static final String INSERT_ORDER_SQL = """
        INSERT INTO orders (
            user_id, total_amount, payment_method, order_status,
            delivery_name, delivery_phone,
            delivery_address_line1, delivery_address_line2,
            delivery_city, delivery_state, delivery_pincode, delivery_country
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    private static final String GET_ORDER_BY_ID_SQL = """
        SELECT * FROM orders WHERE order_id = ?
        """;

    private static final String GET_ORDERS_BY_USER_SQL = """
        SELECT * FROM orders WHERE user_id = ?
        ORDER BY order_date DESC
        """;

    private static final String UPDATE_ORDER_STATUS_SQL = """
        UPDATE orders SET order_status = ? WHERE order_id = ?
        """;

    private static final String DELETE_ORDER_SQL = """
        DELETE FROM orders WHERE order_id = ?
        """;

    private static final String GET_ALL_ORDERS_SQL = """
        SELECT * FROM orders ORDER BY order_date DESC
        """;

    // ================= METHODS =================

    @Override
    public int createOrder(Order order) {
        int generatedId = 0;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ORDER_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getPaymentMethod());
            ps.setString(4, order.getOrderStatus());

            ps.setString(5, order.getDeliveryName());
            ps.setString(6, order.getDeliveryPhone());

            ps.setString(7, order.getDeliveryAddressLine1());
            ps.setString(8, order.getDeliveryAddressLine2());
            ps.setString(9, order.getDeliveryCity());
            ps.setString(10, order.getDeliveryState());
            ps.setString(11, order.getDeliveryPincode());
            ps.setString(12, order.getDeliveryCountry());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in OrderDAO: " + e.getMessage(), e);
        }

        return generatedId;
    }

    @Override
    public Order getOrderById(int orderId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ORDER_BY_ID_SQL)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractOrder(rs);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in OrderDAO: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ORDERS_BY_USER_SQL)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractOrder(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in OrderDAO: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_ORDER_STATUS_SQL)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Database error in OrderDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteOrder(int orderId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_ORDER_SQL)) {

            ps.setInt(1, orderId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Database error in OrderDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL_ORDERS_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractOrder(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in OrderDAO.getAllOrders: " + e.getMessage(), e);
        }
        return list;
    }

    // ================= HELPER =================

    private Order extractOrder(ResultSet rs) throws Exception {
        Order order = new Order();

        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setOrderStatus(rs.getString("order_status"));

        order.setDeliveryName(rs.getString("delivery_name"));
        order.setDeliveryPhone(rs.getString("delivery_phone"));
        order.setDeliveryAddressLine1(rs.getString("delivery_address_line1"));
        order.setDeliveryAddressLine2(rs.getString("delivery_address_line2"));
        order.setDeliveryCity(rs.getString("delivery_city"));
        order.setDeliveryState(rs.getString("delivery_state"));
        order.setDeliveryPincode(rs.getString("delivery_pincode"));
        order.setDeliveryCountry(rs.getString("delivery_country"));

        return order;
    }
}