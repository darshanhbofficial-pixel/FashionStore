package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Category;
import com.fashionstore.model.Product;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/admin/products")
public class AdminProductsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Auth Check
        String email = (user != null) ? user.getEmail() : "";
        String role = (user != null) ? user.getRole() : "";

        if (user == null || (!"ADMIN".equals(role) && !"admin@fashionstore.com".equals(email))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin Only");
            return;
        }

        List<Product> products = productDAO.getAllProducts();
        List<Category> categories = categoryDAO.getAllCategories();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Auth Check
        String email = (user != null) ? user.getEmail() : "";
        String role = (user != null) ? user.getRole() : "";

        if (user == null || (!"ADMIN".equals(role) && !"admin@fashionstore.com".equals(email))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin Only");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("add".equalsIgnoreCase(action)) {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String productName = request.getParameter("productName");
                String brand = request.getParameter("brand");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                String imageUrl = request.getParameter("imageUrl");

                Product product = new Product();
                product.setCategoryId(categoryId);
                product.setProductName(productName);
                product.setBrand(brand);
                product.setDescription(description);
                product.setPrice(price);
                product.setImageUrl(imageUrl != null ? imageUrl : "");
                product.setActive(true);

                boolean success = productDAO.addProduct(product);
                if (success) {
                    response.sendRedirect("products?success=" + java.net.URLEncoder.encode("Product added successfully!", "UTF-8"));
                } else {
                    response.sendRedirect("products?error=" + java.net.URLEncoder.encode("Failed to add product.", "UTF-8"));
                }
            } else if ("update".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String productName = request.getParameter("productName");
                String brand = request.getParameter("brand");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                String imageUrl = request.getParameter("imageUrl");
                boolean isActive = "true".equalsIgnoreCase(request.getParameter("isActive"));

                Product product = new Product();
                product.setProductId(productId);
                product.setCategoryId(categoryId);
                product.setProductName(productName);
                product.setBrand(brand);
                product.setDescription(description);
                product.setPrice(price);
                product.setImageUrl(imageUrl != null ? imageUrl : "");
                product.setActive(isActive);

                boolean success = productDAO.updateProduct(product);
                if (success) {
                    response.sendRedirect("products?success=" + java.net.URLEncoder.encode("Product updated successfully!", "UTF-8"));
                } else {
                    response.sendRedirect("products?error=" + java.net.URLEncoder.encode("Failed to update product.", "UTF-8"));
                }
            } else if ("delete".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                boolean success = productDAO.deleteProduct(productId);
                if (success) {
                    response.sendRedirect("products?success=" + java.net.URLEncoder.encode("Product deleted successfully (soft delete)!", "UTF-8"));
                } else {
                    response.sendRedirect("products?error=" + java.net.URLEncoder.encode("Failed to delete product.", "UTF-8"));
                }
            } else {
                response.sendRedirect("products?error=" + java.net.URLEncoder.encode("Invalid action.", "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("products?error=" + java.net.URLEncoder.encode("Error: " + e.getMessage(), "UTF-8"));
        }
    }
}
