package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import db.DBConnection;

public class Customer extends User {
    private String customerId;
    private String address;
    private List<String> bookingHistory;
    private List<String> preferences;

    public Customer(String userId, String username, String password, String name, String email, String contactInfo, 
                    String customerId, String address, List<String> bookingHistory, List<String> preferences) {
        super(userId, username, password, name, email, contactInfo);
        this.customerId = customerId;
        this.address = address;
        this.bookingHistory = bookingHistory;
        this.preferences = preferences;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getBookingHistory() {
        return bookingHistory;
    }

    public void setBookingHistory(List<String> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public void searchFlights(String origin, String destination, String date) {
        String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, origin);
            stmt.setString(2, destination);
            stmt.setString(3, date);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Flight ID: " + rs.getString("flight_id") +
                                   ",\n Airline: " + rs.getString("airline") +
                                   ",\n Departure: " + rs.getString("departure_time") +
                                   ",\n Arrival: " + rs.getString("arrival_time") +
                                   ",\n Price: " + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println("Error searching for flights: " + e.getMessage());
        }
    }

    public void createBooking(String flightId, String seatClass, int numberOfPassengers) {
        String sql = "INSERT INTO bookings (customer_id, flight_id, seat_class, number_of_passengers, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.customerId);
            stmt.setString(2, flightId);
            stmt.setString(3, seatClass);
            stmt.setInt(4, numberOfPassengers);
            stmt.setString(5, "Reserved");

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Booking created successfully.");
                if (bookingHistory == null) {
                    bookingHistory = new ArrayList<>();
                }
                bookingHistory.add(flightId); 
            }
        } catch (SQLException e) {
            System.out.println("Error creating booking: " + e.getMessage());
        }
    }

    public void viewBookings() {
        String sql = "SELECT * FROM bookings WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.customerId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Booking ID: " + rs.getString("booking_id") +
                                   ", \nFlight ID: " + rs.getString("flight_id") +
                                   ", \nSeat Class: " + rs.getString("seat_class") +
                                   ", \nPassengers: " + rs.getInt("number_of_passengers") +
                                   ", \nStatus: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing bookings: " + e.getMessage());
        }
    }

    public void cancelBooking(String bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ? AND customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookingId);
            stmt.setString(2, this.customerId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Booking canceled successfully.");
                if (bookingHistory != null) {
                    bookingHistory.remove(bookingId);
                }
            } else {
                System.out.println("Booking ID not found or does not belong to this customer.");
            }
        } catch (SQLException e) {
            System.out.println("Error canceling booking: " + e.getMessage());
        }
    }
}
