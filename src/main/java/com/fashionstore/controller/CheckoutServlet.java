package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private CartItemDAO cartItemDAO;
 
    private com.fashionstore.dao.CartDAO cartDAO;

    @Override
    public void init() {
        cartItemDAO = new CartItemDAOImpl();
        cartDAO = new com.fashionstore.dao.impl.CartDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // ✅ GET USER FROM SESSION
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("USER NOT LOGGED IN ❌");
            response.sendRedirect("login");
            return;
        }

        int userId = user.getUserId();
        
        // 🔥 GET ACTUAL CART ID
        com.fashionstore.model.Cart cart = cartDAO.getOrCreateCartByUserId(userId);
        int cartId = (cart != null) ? cart.getCartId() : -1;

        // ✅ AUTO-FILL DATA
        session.setAttribute("name", user.getFullName());
        session.setAttribute("phone", user.getPhone());
        session.setAttribute("address", user.getAddressLine1());
        session.setAttribute("city", user.getCity());
        session.setAttribute("pincode", user.getPincode());

        System.out.println("AUTO-FILL DONE ✅");

        // ✅ GET CART ITEMS
        List<CartItem> cartItems = cartItemDAO.getCartItems(cartId);

        request.setAttribute("cartItems", cartItems);

        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")
               .forward(request, response);
    }
}