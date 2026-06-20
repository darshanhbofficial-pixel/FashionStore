package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/product")
public class ProductDetailsServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            int productId = Integer.parseInt(request.getParameter("id"));

            // 👉 get product
            Product product = productDAO.getProductById(productId);

            // 👉 IMPORTANT: get variants
            List<ProductVariant> variants = productDAO.getVariantsByProductId(productId);

            // 👉 send to JSP
            request.setAttribute("product", product);
            request.setAttribute("variants", variants);

            // 👉 RELATED PRODUCTS (same category, exclude current)
            if (product != null) {
                List<Product> allRelated = productDAO.getProductsByCategory(product.getCategoryId());
                allRelated.removeIf(rp -> rp.getProductId() == productId);
                // limit to 4
                List<Product> related = allRelated.size() > 4 ? allRelated.subList(0, 4) : allRelated;
                request.setAttribute("relatedProducts", related);
            }

            request.getRequestDispatcher("/WEB-INF/views/product-details.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
}