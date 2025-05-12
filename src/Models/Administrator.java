package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;

public class Administrator extends User {

    private String adminId;
    private String securityLevel;

    public Administrator(String userId, String username, String password, String name, String email, String contactInfo,
                         String adminId, String securityLevel) {
        super(userId, username, password, name, email, contactInfo, "Administrator");
        this.adminId = adminId;
        this.securityLevel = securityLevel;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public boolean createUser(String userId, String username, String password, String name, String email, String contact, String role) {
        String sql = "INSERT INTO users (id, username, password, name, email, contact, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, name);
            stmt.setString(5, email);
            stmt.setString(6, contact);
            stmt.setString(7, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    public void modifySystemSettings(String settingName, String newValue) {
        String sql = "UPDATE system_settings SET value = ? WHERE setting_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newValue);
            stmt.setString(2, settingName);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("System setting updated successfully: " + settingName);
            }
        } catch (SQLException e) {
            System.out.println("Error modifying system setting: " + e.getMessage());
        }
    }

    public void viewSystemLogs() {
        String sql = "SELECT * FROM system_logs ORDER BY log_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            System.out.println("System Logs:");
            while (rs.next()) {
                System.out.println("Date: " + rs.getTimestamp("log_date") +
                                   ", Action: " + rs.getString("action") +
                                   ", Performed By: " + rs.getString("performed_by"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing system logs: " + e.getMessage());
        }
    }

    public void manageUserAccess(String userId, String action) {
        String sql;
        switch (action.toLowerCase()) {
            case "enable":
                sql = "UPDATE users SET status = 'active' WHERE user_id = ?";
                break;
            case "disable":
                sql = "UPDATE users SET status = 'inactive' WHERE user_id = ?";
                break;
            default:
                System.out.println("Invalid action. Please specify 'enable' or 'disable'.");
                return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User access updated successfully for user ID: " + userId);
            }
        } catch (SQLException e) {
            System.out.println("Error managing user access: " + e.getMessage());
        }
    }

    public boolean updateUser(String userId, String username, String name, String email, String contact, String role) {
        String sql = "UPDATE users SET username = ?, name = ?, email = ?, contact = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, contact);
            stmt.setString(5, role);
            stmt.setString(6, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
}
