package com.fashionstore.dao;

import java.util.List;

import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;

public interface ProductDAO {

    List<Product> getAllProducts();
    Product getProductById(int productId);
    List<Product> getProductsByCategory(int categoryId);
    List<Product> searchProducts(String keyword);
    List<Product> filterProducts(int categoryId, double minPrice, double maxPrice);
    List<Product> filterProducts(int categoryId, String keyword, double minPrice, double maxPrice);
    List<Product> getProductsByCategoryName(String category);

    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(int productId);

    List<Product> filterByPrice(double minPrice, double maxPrice);

    // ✅ ADD THIS
    List<ProductVariant> getVariantsByProductId(int productId); 
}