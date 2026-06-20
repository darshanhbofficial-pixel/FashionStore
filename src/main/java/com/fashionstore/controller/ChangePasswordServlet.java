package com.fashionstore.controller;

import java.io.IOException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
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

        String currentPassword = request.getParameter("currentPassword");
        String newPassword     = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate current password
        if (currentPassword == null || !currentPassword.equals(user.getPassword())) {
            response.sendRedirect("profile?error=" + java.net.URLEncoder.encode("Current password is incorrect.", "UTF-8") + "#change-password");
            return;
        }

        // Validate new passwords match
        if (newPassword == null || newPassword.trim().length() < 6) {
            response.sendRedirect("profile?error=" + java.net.URLEncoder.encode("New password must be at least 6 characters.", "UTF-8") + "#change-password");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect("profile?error=" + java.net.URLEncoder.encode("New passwords do not match.", "UTF-8") + "#change-password");
            return;
        }

        boolean success = userDAO.updatePassword(user.getUserId(), newPassword.trim());

        if (success) {
            // Update session user password
            user.setPassword(newPassword.trim());
            session.setAttribute("user", user);
            response.sendRedirect("profile?success=" + java.net.URLEncoder.encode("Password changed successfully!", "UTF-8"));
        } else {
            response.sendRedirect("profile?error=" + java.net.URLEncoder.encode("Failed to change password. Try again.", "UTF-8"));
        }
    }
}
