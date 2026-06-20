package com.fashionstore.model;

import java.math.BigDecimal;

public class CartItem {

    private int cartItemId;
    private int cartId;
    private int variantId;
    private int quantity;

    private int productId;
    private String productName;
    private String brand;
    private BigDecimal price;
    private String imageUrl;
    private String size;
    private String color;

    // ✅ Default Constructor
    public CartItem() {
    }

    // ✅ Constructor for DB-level usage
    public CartItem(int cartItemId, int cartId, int variantId, int quantity) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.variantId = variantId;
        this.quantity = quantity;
    }

    // ✅ Constructor for adding items to cart (UI usage)
    public CartItem(int productId, String productName, String brand,
                    BigDecimal price, String imageUrl,
                    String size, String color, int quantity) {

        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
    }

    // ================= GETTERS & SETTERS =================

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // ✅ Utility method
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}