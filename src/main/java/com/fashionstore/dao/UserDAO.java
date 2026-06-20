package com.fashionstore.dao;

import java.util.List;
import com.fashionstore.model.User;

public interface UserDAO {

    // Register
    boolean registerUser(User user);

    // Login
    User loginUser(String email, String password);

    // Fetch
    User getUserById(int userId);
    User getUserByEmail(String email);
    User getUserByPhone(String phone);

    // Update
    boolean updateUser(User user);
    boolean updatePassword(int userId, String newPassword);

    // Delete
    boolean deleteUser(int userId);

    // Validation
    boolean emailExists(String email);
    boolean phoneExists(String phone);

    // Admin / Debug
    List<User> getAllUsers();
}