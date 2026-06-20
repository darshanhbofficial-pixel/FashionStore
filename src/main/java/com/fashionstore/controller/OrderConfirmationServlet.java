package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/order-confirmation")
public class OrderConfirmationServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() {
        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // 1. Get orderId from URL
            String orderIdParam = request.getParameter("orderId");

            if (orderIdParam == null) {
                response.sendRedirect("products");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);

            // 2. Get order from DB
            Order order = orderDAO.getOrderById(orderId);
            System.out.println("Order ID: " + orderId);
            System.out.println("Order object: " + order);

            if (order == null) {
                response.sendRedirect("products"); // fallback
                return;
            }

            // 3. Get order items
            List<OrderItem> orderItems =
                    orderItemDAO.getItemsByOrderId(orderId);

            // 4. Send data to JSP
            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);

            // 5. Forward
            request.getRequestDispatcher("/WEB-INF/views/order-confirmation.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("products");
        }
    }
}