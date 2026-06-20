package com.fashionstore.controller;

import java.io.IOException;

import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        // Stats (Placeholder for now, can be fetched via DAO)
        request.setAttribute("totalOrders", 124);
        request.setAttribute("totalRevenue", 45290.00);
        request.setAttribute("activeUsers", 56);
        request.setAttribute("lowStockItems", 5);

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
