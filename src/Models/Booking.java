package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DBConnection;

public class Booking {
    private String bookingReference, customer, flight, passengers, seatSelections, status, paymentStatus;

    public Booking(String bookingReference, String customer, String flight, String passengers, String seatSelections, String status, String paymentStatus) {
        this.bookingReference = bookingReference;
        this.customer = customer;
        this.flight = flight;
        this.passengers = passengers;
        this.seatSelections = seatSelections;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getPassengers() {
        return passengers;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }

    public String getSeatSelections() {
        return seatSelections;
    }

    public void setSeatSelections(String seatSelections) {
        this.seatSelections = seatSelections;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void addPassenger() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO booking_passengers (booking_id, passenger_id, seat_class) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.bookingReference);
            statement.setString(2, this.passengers); 
            statement.setString(3, this.seatSelections);
            statement.executeUpdate();
            System.out.println("Passenger added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding passenger: " + e.getMessage());
        }
    }

    public void calculateTotalPrice() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT economy_price, business_price, first_class_price FROM flights WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.flight);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double price = 0.0;
                switch (this.seatSelections.toLowerCase()) {
                    case "economy":
                        price = resultSet.getDouble("economy_price");
                        break;
                    case "business":
                        price = resultSet.getDouble("business_price");
                        break;
                    case "first class":
                        price = resultSet.getDouble("first_class_price");
                        break;
                }
                System.out.println("Total price: $" + price);
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total price: " + e.getMessage());
        }
    }

    public void confirmBooking() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE bookings SET status = 'Confirmed' WHERE reference = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.bookingReference);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                this.status = "Confirmed";
                System.out.println("Booking confirmed successfully.");
            } else {
                System.out.println("Failed to confirm booking.");
            }
        } catch (SQLException e) {
            System.out.println("Error confirming booking: " + e.getMessage());
        }
    }

    public void cancelBooking() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE bookings SET status = 'Cancelled' WHERE reference = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.bookingReference);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                this.status = "Cancelled";
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("Failed to cancel booking.");
            }
        } catch (SQLException e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
        }
    }

    public void generateItinerary() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM bookings WHERE reference = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.bookingReference);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Itinerary:");
                System.out.println("Booking Reference: " + resultSet.getString("reference"));
                System.out.println("Customer ID: " + resultSet.getString("customer_id"));
                System.out.println("Flight ID: " + resultSet.getString("flight_id"));
                System.out.println("Status: " + resultSet.getString("status"));
                System.out.println("Payment Status: " + resultSet.getString("payment_status"));
            } else {
                System.out.println("No itinerary found for the given booking reference.");
            }
        } catch (SQLException e) {
            System.out.println("Error generating itinerary: " + e.getMessage());
        }
    }
}
