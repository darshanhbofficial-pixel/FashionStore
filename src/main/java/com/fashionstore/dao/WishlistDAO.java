package com.fashionstore.dao;

import java.util.List;
import com.fashionstore.model.Product;

public interface WishlistDAO {
    boolean addToWishlist(int userId, int productId);
    boolean removeFromWishlist(int userId, int productId);
    boolean isInWishlist(int userId, int productId);
    List<Product> getWishlistByUserId(int userId);
    int getWishlistCount(int userId);
}
