package com.fashionstore.dao;

import java.math.BigDecimal;
import java.util.List;

import com.fashionstore.model.CartItem;

public interface CartItemDAO {

    // ✅ Add item to cart
    boolean addCartItem(CartItem cartItem);

    // ✅ Update quantity using cartItemId
    boolean updateCartItemQuantity(int cartItemId, int quantity);

    // ✅ Update quantity using cartId + variantId
    boolean updateCartItemQuantityByCartAndVariant(int cartId, int variantId, int quantity);

    // ✅ Remove item from cart
    boolean removeCartItem(int cartItemId);

    // ✅ Get all items of a cart
    List<CartItem> getCartItems(int cartId);

    // ✅ Get total cart value
    BigDecimal getCartTotal(int cartId);
}