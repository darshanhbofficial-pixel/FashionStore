package com.fashionstore.dao;

import java.util.List;
import com.fashionstore.model.ProductVariant;

public interface ProductVariantDAO {
	

    // Fetch all variants for a product (S, M, L, XL)
    List<ProductVariant> getVariantsByProductId(int productId);

    // Fetch specific variant
    ProductVariant getVariantById(int variantId);

    // Get variant using product + size (very important for cart)
    ProductVariant getVariantByProductAndSize(int productId, String size);

    // Stock check
    int getStockByVariantId(int variantId);

    // Update stock (after order placement)
    boolean updateStock(int variantId, int newQuantity);

    // CRUD
    boolean addVariant(ProductVariant variant);

    boolean updateVariant(ProductVariant variant);

    boolean deleteVariant(int variantId);
    
    void reduceStock(int variantId, int quantity);
}