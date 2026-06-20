package com.fashionstore.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.ProductVariantDAO;

import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.dao.impl.ProductVariantDAOImpl;

import com.fashionstore.model.CartItem;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/place-order")
public class PlaceOrderServlet extends HttpServlet {

    private CartItemDAO cartItemDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ProductVariantDAO productVariantDAO;
    private com.fashionstore.dao.CartDAO cartDAO;

    @Override
    public void init() {
        cartItemDAO = new CartItemDAOImpl();
        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
        productVariantDAO = new ProductVariantDAOImpl();
        cartDAO = new com.fashionstore.dao.impl.CartDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // ✅ USER
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = user.getUserId();

        // ✅ FORM DATA
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String pincode = request.getParameter("pincode");
        String paymentMethod = request.getParameter("paymentMethod");
        String couponCode = request.getParameter("couponCode");
        String saveAddress = request.getParameter("saveAddress");

        // Save address to session if requested
        if ("true".equals(saveAddress)) {
            session.setAttribute("name", name);
            session.setAttribute("phone", phone);
            session.setAttribute("address", address);
            session.setAttribute("city", city);
            session.setAttribute("pincode", pincode);
        }
        
        // 🔥 GET ACTUAL CART ID
        com.fashionstore.model.Cart cart = cartDAO.getOrCreateCartByUserId(userId);
        int cartId = (cart != null) ? cart.getCartId() : -1;

        // ✅ CART ITEMS
        List<CartItem> cartItems = cartItemDAO.getCartItems(cartId);

        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        // ✅ TOTAL CALCULATION
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            BigDecimal price = item.getPrice();
            int qty = item.getQuantity();
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(qty)));
        }

        // Apply coupon discount
        double finalTotal = totalAmount.doubleValue();
        if (couponCode != null && couponCode.equalsIgnoreCase("FASHION10")) {
            finalTotal = finalTotal * 0.90; // 10% off
        }

        // ✅ CREATE ORDER
        Order order = new Order();

        order.setUserId(userId);
        order.setTotalAmount(finalTotal);
        order.setOrderStatus("PLACED");
        order.setPaymentMethod(paymentMethod);

        // DELIVERY DETAILS
        order.setDeliveryName(name);
        order.setDeliveryPhone(phone);
        order.setDeliveryAddressLine1(address);
        order.setDeliveryCity(city);
        order.setDeliveryPincode(pincode);

        // OPTIONAL (if you have fields)
        order.setDeliveryState("Karnataka");
        order.setDeliveryCountry("India");

        // ✅ SAVE ORDER
        int orderId = orderDAO.createOrder(order);

        // 🔥 DEBUG
        System.out.println("ORDER ID CREATED: " + orderId);

        if (orderId <= 0) {
            System.out.println("ORDER INSERT FAILED ❌");
            response.sendRedirect("cart");
            return;
        }

        // ✅ CREATE ORDER ITEMS
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cartItems) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrderId(orderId);
            orderItem.setVariantId(item.getVariantId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice().doubleValue());

            orderItems.add(orderItem);

            // STOCK REDUCE
            productVariantDAO.reduceStock(item.getVariantId(), item.getQuantity());
        }

        // SAVE ITEMS
        orderItemDAO.addOrderItems(orderItems);

        // ✅ CLEAR CART
        for (CartItem item : cartItems) {
            cartItemDAO.removeCartItem(item.getCartItemId());
        }

        // ✅ FINAL REDIRECT (MOST IMPORTANT)
        response.sendRedirect("order-confirmation?orderId=" + orderId);
    }
}