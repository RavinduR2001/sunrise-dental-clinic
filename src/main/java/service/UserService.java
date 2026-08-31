package com.sunrisedental.service;

import com.sunrisedental.dao.UserDAO;
import com.sunrisedental.models.User;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Authenticate user
    public User authenticate(String username, String password) {
        String hashedPassword = hashPassword(password);
        return userDAO.getUserByCredentials(username, hashedPassword);
    }

    // Get user by ID
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Create new user
    public boolean createUser(User user) {
        // Hash password before storing
        user.setPasswordHash(hashPassword(user.getPasswordHash()));
        return userDAO.createUser(user);
    }

    // Update user
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    // Delete user
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }

    // Hash password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback (not recommended for production)
        }
    }

    // Validate user input
    public boolean validateUser(User user) {
        if (user == null) return false;
        if (user.getUsername() == null || user.getUsername().isEmpty()) return false;
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) return false;
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) return false;
        if (user.getLastName() == null || user.getLastName().isEmpty()) return false;
        if (user.getEmail() == null || user.getEmail().isEmpty()) return false;
        if (user.getRole() == null || user.getRole().isEmpty()) return false;
        return true;
    }
}