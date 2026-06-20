package com.fashionstore.dao;

import com.fashionstore.model.Cart;

public interface CartDAO {

    // Create cart
    boolean createCart(int userId);

    // Fetch cart
    Cart getCartById(int cartId);

    Cart getCartByUserId(int userId);

    // Smart method (very useful)
    Cart getOrCreateCartByUserId(int userId);

    // Delete
    boolean deleteCart(int cartId);

    // Check existence
    boolean cartExistsByUserId(int userId);
}