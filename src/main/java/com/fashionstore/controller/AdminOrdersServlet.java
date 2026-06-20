package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/admin/orders")
public class AdminOrdersServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
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

        List<Order> orders = orderDAO.getAllOrders();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(request, response);
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

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");

            boolean success = orderDAO.updateOrderStatus(orderId, status);
            if (success) {
                response.sendRedirect("orders?success=" + java.net.URLEncoder.encode("Order status updated to " + status + "!", "UTF-8"));
            } else {
                response.sendRedirect("orders?error=" + java.net.URLEncoder.encode("Failed to update order status.", "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("orders?error=" + java.net.URLEncoder.encode("Error: " + e.getMessage(), "UTF-8"));
        }
    }
}
