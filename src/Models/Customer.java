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
        super(userId, username, password, name, email, contactInfo, "Customer");
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

    public List<Flight> searchFlights(String flightNumber) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE flight_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flightNumber);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String airline = rs.getString("airline");
                String origin = rs.getString("origin");
                String destination = rs.getString("destination");
                String departureTime = rs.getString("departure_time");
                String arrivalTime = rs.getString("arrival_time");
                String availableSeats = rs.getInt("economy_seats") + "," +
                                        rs.getInt("business_seats") + "," +
                                        rs.getInt("first_class_seats");
                String prices = rs.getDouble("economy_price") + "," +
                                rs.getDouble("business_price") + "," +
                                rs.getDouble("first_class_price");

                Flight flight = new Flight(flightNumber, airline, origin, destination, departureTime, arrivalTime, availableSeats, prices);
                flights.add(flight);
            }
        } catch (SQLException e) {
            System.out.println("Error searching for flights: " + e.getMessage());
        }
        return flights;
    }

    public void createBooking(String flightId, String seatClass) {
        String sql = "INSERT INTO bookings (reference, customer_id, flight_id, seat_class, status, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String bookingReference = "REF" + System.currentTimeMillis(); // Generate a unique booking reference
            stmt.setString(1, bookingReference);
            stmt.setString(2, this.customerId);
            stmt.setString(3, flightId);
            stmt.setString(4, seatClass);
            stmt.setString(5, "Reserved");
            stmt.setString(6, "Pending");

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Booking created successfully.");
                if (bookingHistory == null) {
                    bookingHistory = new ArrayList<>();
                }
                bookingHistory.add(bookingReference); // Add the booking reference to the history
            }
        } catch (SQLException e) {
            System.out.println("Error creating booking: " + e.getMessage());
        }
    }

    

    public boolean cancelBooking(String bookingReference) {
        String sql = "DELETE FROM bookings WHERE reference = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookingReference);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking canceled successfully.");
                return true;
            } else {
                System.out.println("Booking not found or could not be canceled.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error canceling booking: " + e.getMessage());
            return false;
        }
    }

   public boolean bookFlight(String flightNumber) {
    String sql = "INSERT INTO bookings (reference, customer_id, flight_id, status, payment_status) " +
                 "VALUES (?, ?, (SELECT id FROM flights WHERE flight_number = ?), 'Confirmed', 'Pending')";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        String bookingReference = "REF" + System.currentTimeMillis(); // Generate a unique reference
        stmt.setString(1, bookingReference);
        stmt.setString(2, this.getCustomerId());
        stmt.setString(3, flightNumber);

        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Flight booked successfully.");
            return true;
        } else {
            System.out.println("Failed to book flight. Flight number may not exist.");
            return false;
        }
    } catch (SQLException e) {
        System.out.println("Error booking flight: " + e.getMessage());
        return false;
    }
}

public List<Booking> getBookings() {
    List<Booking> bookings = new ArrayList<>();
    String sql = "SELECT b.reference, f.flight_number, b.status, b.payment_status " +
                 "FROM bookings b " +
                 "JOIN flights f ON b.flight_id = f.id " +
                 "WHERE b.customer_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, this.getCustomerId());

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String reference = rs.getString("reference");
            String flightNumber = rs.getString("flight_number");
            String status = rs.getString("status");
            String paymentStatus = rs.getString("payment_status");

            bookings.add(new Booking(reference, this.getCustomerId(), flightNumber, null, null, status, paymentStatus));
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving bookings: " + e.getMessage());
    }
    return bookings;
}
}
