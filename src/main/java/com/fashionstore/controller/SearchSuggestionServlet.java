package com.fashionstore.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/search-suggestions")
public class SearchSuggestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String query = request.getParameter("q");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (query == null || query.trim().length() < 2) {
            out.print("[]");
            return;
        }

        List<Product> products = productDAO.searchProducts(query);
        
        // Limit to top 5 suggestions
        int limit = Math.min(products.size(), 5);
        StringBuilder json = new StringBuilder("[");
        
        for (int i = 0; i < limit; i++) {
            Product p = products.get(i);
            json.append("{");
            json.append("\"id\":").append(p.getProductId()).append(",");
            json.append("\"name\":\"").append(p.getProductName().replace("\"", "\\\"")).append("\",");
            json.append("\"brand\":\"").append(p.getBrand().replace("\"", "\\\"")).append("\",");
            json.append("\"image\":\"").append(p.getImageUrl()).append("\"");
            json.append("}");
            if (i < limit - 1) json.append(",");
        }
        
        json.append("]");
        out.print(json.toString());
    }
}
