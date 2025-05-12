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
        super(userId, username, password, name, email, contactInfo);
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

    public void manageFlights(String flightId, String action) {
        String sql;
        switch (action.toLowerCase()) {
            case "add":
                sql = "INSERT INTO flights (flight_id, airline, origin, destination, departure_time, arrival_time, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    // Example values for adding a flight
                    stmt.setString(1, flightId);
                    stmt.setString(2, "Example Airline");
                    stmt.setString(3, "Origin City");
                    stmt.setString(4, "Destination City");
                    stmt.setString(5, "2025-05-15 10:00:00");
                    stmt.setString(6, "2025-05-15 14:00:00");
                    stmt.setDouble(7, 200.0);

                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Flight added successfully.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error adding flight: " + e.getMessage());
                }
                break;

            case "update":
                sql = "UPDATE flights SET price = ? WHERE flight_id = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setDouble(1, 250.0); // Example updated price
                    stmt.setString(2, flightId);

                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Flight updated successfully.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error updating flight: " + e.getMessage());
                }
                break;

            case "delete":
                sql = "DELETE FROM flights WHERE flight_id = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, flightId);

                    int rowsDeleted = stmt.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("Flight deleted successfully.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error deleting flight: " + e.getMessage());
                }
                break;

            default:
                System.out.println("Invalid action. Please specify 'add', 'update', or 'delete'.");
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
}
