package com.fashionstore.controller;

import java.io.IOException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Refresh user data from DB
        User freshUser = userDAO.getUserById(user.getUserId());
        if (freshUser != null) {
            session.setAttribute("user", freshUser);
            user = freshUser;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Read form fields
        String fullName    = request.getParameter("fullName");
        String phone       = request.getParameter("phone");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String city        = request.getParameter("city");
        String state       = request.getParameter("state");
        String pincode     = request.getParameter("pincode");
        String country     = request.getParameter("country");

        // Update user object
        user.setFullName(fullName != null ? fullName.trim() : user.getFullName());
        user.setPhone(phone != null ? phone.trim() : user.getPhone());
        user.setAddressLine1(addressLine1 != null ? addressLine1.trim() : user.getAddressLine1());
        user.setAddressLine2(addressLine2 != null ? addressLine2.trim() : user.getAddressLine2());
        user.setCity(city != null ? city.trim() : user.getCity());
        user.setState(state != null ? state.trim() : user.getState());
        user.setPincode(pincode != null ? pincode.trim() : user.getPincode());
        user.setCountry(country != null ? country.trim() : user.getCountry());

        boolean success = userDAO.updateUser(user);

        if (success) {
            // Refresh session
            User updated = userDAO.getUserById(user.getUserId());
            if (updated != null) session.setAttribute("user", updated);
            response.sendRedirect("profile?success=" + java.net.URLEncoder.encode("Profile updated successfully!", "UTF-8"));
        } else {
            response.sendRedirect("profile?error=" + java.net.URLEncoder.encode("Failed to update profile. Please try again.", "UTF-8"));
        }
    }
}
