package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/products")
public class ProductsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // 🔹 PARAMETERS
            String keyword = request.getParameter("keyword");
            String collection = request.getParameter("collection"); // 🔥 FROM HOME SLIDER

            String categoryName = request.getParameter("category");   // 🔥 FROM HOME PAGE
            String categoryParam = request.getParameter("categoryId"); // 🔥 OLD FILTER

            String minParam = request.getParameter("minPrice");
            String maxParam = request.getParameter("maxPrice");
            String productIdParam = request.getParameter("productId");

            // Fetch all products for the dropdown filter
            List<Product> allProductsForFilter = productDAO.getAllProducts();
            request.setAttribute("allProductsForFilter", allProductsForFilter);

            List<Product> products;

            // 🔹 CASE 0: SPECIFIC PRODUCT ID
            if (productIdParam != null && !productIdParam.isEmpty()) {
                int productId = Integer.parseInt(productIdParam);
                Product p = productDAO.getProductById(productId);
                products = new java.util.ArrayList<>();
                if (p != null) {
                    // Check price if provided
                    if (minParam != null && !minParam.isEmpty() && maxParam != null && !maxParam.isEmpty()) {
                        double min = Double.parseDouble(minParam);
                        double max = Double.parseDouble(maxParam);
                        if (p.getPrice() >= min && p.getPrice() <= max) {
                            products.add(p);
                        }
                    } else {
                        products.add(p);
                    }
                }
            }
            // 🔹 CASE 1: SEARCH (+ Optional Price)
            else if (keyword != null && !keyword.isEmpty()) {

                if (minParam != null && !minParam.isEmpty() && maxParam != null && !maxParam.isEmpty()) {
                    double min = Double.parseDouble(minParam);
                    double max = Double.parseDouble(maxParam);
                    
                    if (categoryParam != null && !categoryParam.isEmpty()) {
                        int categoryId = Integer.parseInt(categoryParam);
                        products = productDAO.filterProducts(categoryId, keyword, min, max);
                    } else {
                        // We don't have a specific DAO method for keyword + price only, 
                        // but we can filter the search results in memory for simplicity
                        products = productDAO.searchProducts(keyword);
                        products.removeIf(p -> p.getPrice() < min || p.getPrice() > max);
                    }
                } else {
                    products = productDAO.searchProducts(keyword);
                }
                
                // 🔥 FIX: Ensure Women's items don't show up in Men's search
                if ("Men".equalsIgnoreCase(keyword)) {
                    products.removeIf(p -> p.getProductName() != null && p.getProductName().toLowerCase().contains("women"));
                }
            }

            // 🔹 CASE 2: CUSTOM COLLECTION
            else if (collection != null && "Accessories".equalsIgnoreCase(collection)) {
                products = new java.util.ArrayList<>();
                products.addAll(productDAO.getProductsByCategoryName("Watches"));
                products.addAll(productDAO.getProductsByCategoryName("Caps"));
                products.addAll(productDAO.getProductsByCategoryName("Shoes"));
            }
            else if (collection != null && "Clothes".equalsIgnoreCase(collection)) {
                products = productDAO.getAllProducts();
                if (products != null) {
                    List<Product> accessories = new java.util.ArrayList<>();
                    accessories.addAll(productDAO.getProductsByCategoryName("Watches"));
                    accessories.addAll(productDAO.getProductsByCategoryName("Caps"));
                    accessories.addAll(productDAO.getProductsByCategoryName("Shoes"));
                    
                    List<Integer> accessoryIds = accessories.stream()
                        .map(Product::getProductId)
                        .collect(java.util.stream.Collectors.toList());
                        
                    products.removeIf(p -> accessoryIds.contains(p.getProductId()));
                }
            }

            // 🔹 CASE 3: CATEGORY NAME
            else if (categoryName != null && !categoryName.isEmpty()) {
                products = productDAO.getProductsByCategoryName(categoryName);
            }

            // 🔹 CASE 4: PRICE FILTER (GLOBAL OR WITH CATEGORY ID)
            else if (minParam != null && !minParam.isEmpty() && maxParam != null && !maxParam.isEmpty()) {
                double min = Double.parseDouble(minParam);
                double max = Double.parseDouble(maxParam);

                if (categoryParam != null && !categoryParam.isEmpty()) {
                    int categoryId = Integer.parseInt(categoryParam);
                    products = productDAO.filterProducts(categoryId, min, max);
                } else {
                    products = productDAO.filterByPrice(min, max);
                }
            }

            // 🔹 CASE 5: CATEGORY ID ONLY
            else if (categoryParam != null && !categoryParam.isEmpty()) {
                int categoryId = Integer.parseInt(categoryParam);
                products = productDAO.getProductsByCategory(categoryId);
            }

            // 🔹 DEFAULT
            else {
                products = productDAO.getAllProducts();
            }



            // 🔹 SEND DATA
            request.setAttribute("products", products);

            request.getRequestDispatcher("/WEB-INF/views/products.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }
}