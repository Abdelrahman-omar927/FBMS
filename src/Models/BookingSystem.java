package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DBConnection;

public class BookingSystem {
    private String users, flights, bookings, payments, origin, destination;

    public BookingSystem(String users, String flights, String bookings, String payments, String origin, String destination) {
        this.users = users;
        this.flights = flights;
        this.bookings = bookings;
        this.payments = payments;
        this.origin = origin;
        this.destination = destination;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getFlights() {
        return flights;
    }

    public void setFlights(String flights) {
        this.flights = flights;
    }

    public String getBookings() {
        return bookings;
    }

    public void setBookings(String bookings) {
        this.bookings = bookings;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void searchFlights() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, this.getOrigin());
            statement.setString(2, this.getDestination());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Flight Number: " + resultSet.getString("flight_number"));
                System.out.println("Airline: " + resultSet.getString("airline"));
                System.out.println("Departure Time: " + resultSet.getString("departure_time"));
                System.out.println("Arrival Time: " + resultSet.getString("arrival_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void processPayment(String bookingReference, double amount, String status) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO payments (booking_id, amount, status) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, bookingReference);
            statement.setDouble(2, amount);
            statement.setString(3, status);
            statement.executeUpdate();
            System.out.println("Payment processed successfully.");
        } catch (SQLException e) {
            System.out.println("Error processing payment: " + e.getMessage());
        }
    }

    public void createBooking(String reference, String customerId, String flightId, String status, String paymentStatus) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO bookings (reference, customer_id, flight_id, status, payment_status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reference);
            statement.setString(2, customerId);
            statement.setString(3, flightId);
            statement.setString(4, status);
            statement.setString(5, paymentStatus);
            statement.executeUpdate();
            System.out.println("Booking created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating booking: " + e.getMessage());
        }
    }

    public void cancelBooking(String reference) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE bookings SET status = 'Cancelled' WHERE reference = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reference);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("Failed to cancel booking.");
            }
        } catch (SQLException e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
        }
    }

    public void generateTicket(String reference) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM bookings WHERE reference = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reference);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Ticket:");
                System.out.println("Booking Reference: " + resultSet.getString("reference"));
                System.out.println("Customer ID: " + resultSet.getString("customer_id"));
                System.out.println("Flight ID: " + resultSet.getString("flight_id"));
                System.out.println("Status: " + resultSet.getString("status"));
                System.out.println("Payment Status: " + resultSet.getString("payment_status"));
            } else {
                System.out.println("No ticket found for the given booking reference.");
            }
        } catch (SQLException e) {
            System.out.println("Error generating ticket: " + e.getMessage());
        }
    }
}
