package com.fashionstore.dao;

import java.util.List;
import com.fashionstore.model.OrderItem;

public interface OrderItemDAO {

    // Add item to order
    boolean addOrderItem(OrderItem item);

    // Add multiple items (useful during checkout)
    boolean addOrderItems(List<OrderItem> items);

    // Fetch items of a specific order
    List<OrderItem> getItemsByOrderId(int orderId);
    
   

    // Delete item (optional)
    boolean deleteOrderItem(int orderItemId);

    // Clear all items of an order (optional)
    boolean clearOrderItems(int orderId);
}