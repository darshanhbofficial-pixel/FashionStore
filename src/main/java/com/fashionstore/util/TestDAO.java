package com.fashionstore.util;

import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

public class TestDAO {

    public static void main(String[] args) {

        ProductDAO productDAO = new ProductDAOImpl();

        // 1. Get All Products
        List<Product> products = productDAO.getAllProducts();

        System.out.println("---- ALL PRODUCTS ----");

        for (Product p : products) {
            System.out.println(
                p.getProductId() + " | " +
                p.getProductName() + " | " +
                p.getBrand() + " | ₹" +
                p.getPrice()
            );
        }

        // 2. Get Product by ID
        System.out.println("\n---- PRODUCT BY ID ----");

        Product product = productDAO.getProductById(1);

        if (product != null) {
            System.out.println(product.getProductName() + " - ₹" + product.getPrice());
        } else {
            System.out.println("Product not found");
        }

        // 3. Search Product
        System.out.println("\n---- SEARCH 'shirt' ----");

        List<Product> searchList = productDAO.searchProducts("shirt");

        for (Product p : searchList) {
            System.out.println(p.getProductName());
        }

        // 4. Filter by Category
        System.out.println("\n---- MEN CATEGORY ----");

        List<Product> menProducts = productDAO.getProductsByCategory(1);

        for (Product p : menProducts) {
            System.out.println(p.getProductName());
        }

        // 5. Filter by Price
        System.out.println("\n---- PRICE RANGE 500 - 1500 ----");

        List<Product> filtered = productDAO.filterProducts(1, 500, 1500);

        for (Product p : filtered) {
            System.out.println(p.getProductName() + " - ₹" + p.getPrice());
        }
    }
}