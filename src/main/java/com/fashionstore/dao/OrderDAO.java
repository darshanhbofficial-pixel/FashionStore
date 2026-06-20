package com.fashionstore.dao;

import java.util.List;
import com.fashionstore.model.Order;

public interface OrderDAO {

    // Create new order (returns generated orderId)
    int createOrder(Order order);

    // Fetch single order
    Order getOrderById(int orderId);

    // Fetch all orders for a user
    List<Order> getOrdersByUserId(int userId);

    // Update order status (PLACED, SHIPPED, DELIVERED, CANCELLED)
    boolean updateOrderStatus(int orderId, String status);

    // Fetch all orders in the system (for admin)
    List<Order> getAllOrders();

    // Delete order (optional, rarely used)
    boolean deleteOrder(int orderId);
}