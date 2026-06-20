package com.fashionstore.model;

public class Cart {

    private int cartId;
    private int userId;

    // Default Constructor
    public Cart() {
    }

    // Getters and Setters

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}