package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.WishlistDAO;
import com.fashionstore.dao.impl.WishlistDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WishlistDAO wishlistDAO;

    @Override
    public void init() throws ServletException {
        wishlistDAO = new WishlistDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Product> watchlistedProducts = wishlistDAO.getWishlistByUserId(user.getUserId());

        request.setAttribute("watchlistedProducts", watchlistedProducts);
        request.getRequestDispatcher("/WEB-INF/views/wishlist.jsp").forward(request, response);
    }
}
