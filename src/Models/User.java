package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DBConnection;

public class User {
    private String userId, username, password, name, email, contactInfo, role;

    public User(String userId, String username, String password, String name, String email, String contactInfo, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.contactInfo = contactInfo;
        this.role = role;
    }

    public static User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String role = rs.getString("role");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String contact = rs.getString("contact");
                    String pass = rs.getString("password");
                    switch (role) {
                        case "Customer":
                            return new Customer(id, username, pass, name, email, contact, id, null, null, null);
                        case "Agent":
                            return new Agent(id, username, pass, name, email, contact, id, null, null);
                        case "Administrator":
                            return new Administrator(id, username, pass, name, email, contact, id, null);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Login failed: " + e.getMessage());
        }
        return null;
    }

    public static boolean register(String username, String password, String name,
                                   String email, String contact, String role) {
        // Remove ID from SQL and parameters
        String sql = "INSERT INTO users (username, password, name, email, contact, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, email);
            stmt.setString(5, contact);
            stmt.setString(6, role);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
            return false;
        }
    }
    public boolean updateProfile(String name, String email, String contactInfo) {
        String sql = "UPDATE users SET name = ?, email = ?, contact = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, contactInfo);
            stmt.setString(4, this.userId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                this.name = name;
                this.email = email;
                this.contactInfo = contactInfo;
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Profile update failed: " + e.getMessage());
        }
        return false;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getRole() {
        return role;
    }
}