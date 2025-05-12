package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:data/flight_booking.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC"); // Explicitly load the driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found.", e);
        }
        return DriverManager.getConnection(URL);
    }

    public static void enableWalMode() {
        String sql = "PRAGMA journal_mode=WAL;";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("WAL mode enabled.");
        } catch (SQLException e) {
            System.out.println("Error enabling WAL mode: " + e.getMessage());
        }
    }
}
