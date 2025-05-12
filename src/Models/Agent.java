package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;

public class Agent extends User {
    private String agentId;
    private String department;
    private String commission;

    public Agent(String userId, String username, String password, String name, String email, String contactInfo,
                 String agentId, String department, String commission) {
        super(userId, username, password, name, email, contactInfo, "Agent");
        this.agentId = agentId;
        this.department = department;
        this.commission = commission;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public boolean manageFlights(String flightNumber, String airline, String origin, String destination,
                             String departureTime, String arrivalTime, double economyPrice, String action) {
        String sql;
        if ("add".equalsIgnoreCase(action)) {
            sql = "INSERT INTO flights (flight_number, airline, origin, destination, departure_time, arrival_time, economy_price) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if ("update".equalsIgnoreCase(action)) {
            sql = "UPDATE flights SET airline = ?, origin = ?, destination = ?, departure_time = ?, arrival_time = ?, economy_price = ? " +
                  "WHERE flight_number = ?";
        } else if ("delete".equalsIgnoreCase(action)) {
            sql = "DELETE FROM flights WHERE flight_number = ?";
        } else {
            return false; // Invalid action
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if ("add".equalsIgnoreCase(action)) {
                stmt.setString(1, flightNumber);
                stmt.setString(2, airline);
                stmt.setString(3, origin);
                stmt.setString(4, destination);
                stmt.setString(5, departureTime);
                stmt.setString(6, arrivalTime);
                stmt.setDouble(7, economyPrice);
            } else if ("update".equalsIgnoreCase(action)) {
                stmt.setString(1, airline);
                stmt.setString(2, origin);
                stmt.setString(3, destination);
                stmt.setString(4, departureTime);
                stmt.setString(5, arrivalTime);
                stmt.setDouble(6, economyPrice);
                stmt.setString(7, flightNumber);
            } else if ("delete".equalsIgnoreCase(action)) {
                stmt.setString(1, flightNumber);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error managing flights: " + e.getMessage());
            return false;
        }
    }

    public void createBookingForCustomer(String customerId, String flightId, String seatClass, int numberOfPassengers) {
        String sql = "INSERT INTO bookings (customer_id, flight_id, seat_class, number_of_passengers, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            stmt.setString(2, flightId);
            stmt.setString(3, seatClass);
            stmt.setInt(4, numberOfPassengers);
            stmt.setString(5, "Reserved");

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Booking created successfully for customer ID: " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("Error creating booking for customer: " + e.getMessage());
        }
    }

    public void modifyBooking(String bookingId, String flightId, String seatClass, int numberOfPassengers) {
        String sql = "UPDATE bookings SET flight_id = ?, seat_class = ?, number_of_passengers = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flightId);
            stmt.setString(2, seatClass);
            stmt.setInt(3, numberOfPassengers);
            stmt.setString(4, bookingId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Booking modified successfully with ID: " + bookingId);
            }
        } catch (SQLException e) {
            System.out.println("Error modifying booking: " + e.getMessage());
        }
    }

    public void generateReports() {
        String sql = "SELECT flight_id, COUNT(*) AS total_bookings FROM bookings GROUP BY flight_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            System.out.println("Flight Reports:");
            while (rs.next()) {
                System.out.println("Flight ID: " + rs.getString("flight_id") +
                                   ", Total Bookings: " + rs.getInt("total_bookings"));
            }
        } catch (SQLException e) {
            System.out.println("Error generating reports: " + e.getMessage());
        }
    }

    public boolean cancelBooking(String bookingReference) {
        String sql = "DELETE FROM bookings WHERE reference = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookingReference);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error canceling booking: " + e.getMessage());
            return false;
        }
    }
}
