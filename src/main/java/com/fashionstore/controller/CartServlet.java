package com.fashionstore.controller;

import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

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

        String action = request.getParameter("action");

        if (action == null) action = "view";

        switch (action) {

            case "add":
                addToCart(request, response);
                break;

            case "update":
                updateCart(request, response);
                break;

            case "remove":
                removeItem(request, response);
                break;

            default:
                viewCart(request, response);
                break;
        }
    }

    // ✅ ADD ITEM
    
    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	
        HttpSession session = request.getSession();
        Object loggedInUser = session.getAttribute("user");

        if (loggedInUser == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = ((com.fashionstore.model.User)loggedInUser).getUserId();
        
        // 🔥 GET OR CREATE ACTUAL CART ID
        com.fashionstore.model.Cart cart = cartDAO.getOrCreateCartByUserId(userId);
        if (cart == null) {
            response.sendRedirect("cart?error=cart_creation_failed");
            return;
        }
        int cartId = cart.getCartId();

        int variantId = Integer.parseInt(request.getParameter("variantId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        CartItem item = new CartItem();
        item.setCartId(cartId); 
        item.setVariantId(variantId);
        item.setQuantity(quantity);

        cartItemDAO.addCartItem(item);

        response.sendRedirect("cart");
    }

    // ✅ UPDATE ITEM
    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        cartItemDAO.updateCartItemQuantity(cartItemId, quantity);

        response.sendRedirect("cart");
    }

    // ✅ REMOVE ITEM
    private void removeItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));

        cartItemDAO.removeCartItem(cartItemId);

        response.sendRedirect("cart");
    }

    // ✅ VIEW CART
    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object loggedInUser = session.getAttribute("user");
        List<CartItem> items;

        if (loggedInUser != null) {
            int userId = ((com.fashionstore.model.User)loggedInUser).getUserId();
            
            // 🔥 GET ACTUAL CART ID
            com.fashionstore.model.Cart cart = cartDAO.getOrCreateCartByUserId(userId);
            int cartId = (cart != null) ? cart.getCartId() : -1;
            
            items = cartItemDAO.getCartItems(cartId);
        } else {
            items = new java.util.ArrayList<>();
        }

        request.setAttribute("cartItems", items);

        request.getRequestDispatcher("/WEB-INF/views/cart.jsp")
               .forward(request, response);
    }

    // ✅ REMOVED HARDCODED CART ID LOGIC
    /*
    private int getCartId(HttpServletRequest request) {
        ...
    }
    */
}