package Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.DBConnection;

public class FileManager {

    // Save users to the database
    public void saveUsers(List<User> users) {
        String sql = "INSERT INTO users (username, password, name, email, contact, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (User user : users) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getName());
                statement.setString(4, user.getEmail());
                statement.setString(5, user.getContactInfo());
                statement.setString(6, user.getRole());
                statement.addBatch();
            }
            statement.executeBatch();
            System.out.println("Users saved successfully.");
        } catch (SQLException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // Load users from the database
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String contact = resultSet.getString("contact");
                String role = resultSet.getString("role");

                User user;
                switch (role.toLowerCase()) {
                    case "customer":
                        user = new Customer(id, username, password, name, email, contact, null, null, null, null);
                        break;
                    case "agent":
                        user = new Agent(id, username, password, name, email, contact, null, null, null);
                        break;
                    case "administrator":
                        user = new Administrator(id, username, password, name, email, contact, null, null);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown role: " + role);
                }
                users.add(user);
            }
            System.out.println("Users loaded successfully.");
        } catch (SQLException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    // Save flights to the database
    public void saveFlights(List<Flight> flights) {
        String sql = "INSERT INTO flights (flight_number, airline, origin, destination, departure_time, arrival_time, economy_seats, business_seats, first_class_seats, economy_price, business_price, first_class_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Flight flight : flights) {
                statement.setString(1, flight.getFlightNumber());
                statement.setString(2, flight.getAirline());
                statement.setString(3, flight.getOrigin());
                statement.setString(4, flight.getDestination());
                statement.setString(5, flight.getDepartureTime());
                statement.setString(6, flight.getArrivalTime());
                statement.setInt(7, Integer.parseInt(flight.getAvailableSeats().split(",")[0])); // Economy seats
                statement.setInt(8, Integer.parseInt(flight.getAvailableSeats().split(",")[1])); // Business seats
                statement.setInt(9, Integer.parseInt(flight.getAvailableSeats().split(",")[2])); // First-class seats
                statement.setDouble(10, Double.parseDouble(flight.getPrices().split(",")[0])); // Economy price
                statement.setDouble(11, Double.parseDouble(flight.getPrices().split(",")[1])); // Business price
                statement.setDouble(12, Double.parseDouble(flight.getPrices().split(",")[2])); // First-class price
                statement.addBatch();
            }
            statement.executeBatch();
            System.out.println("Flights saved successfully.");
        } catch (SQLException e) {
            System.out.println("Error saving flights: " + e.getMessage());
        }
    }

    // Load flights from the database
    public List<Flight> loadFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String flightNumber = resultSet.getString("flight_number");
                String airline = resultSet.getString("airline");
                String origin = resultSet.getString("origin");
                String destination = resultSet.getString("destination");
                String departureTime = resultSet.getString("departure_time");
                String arrivalTime = resultSet.getString("arrival_time");
                String availableSeats = resultSet.getInt("economy_seats") + "," +
                                        resultSet.getInt("business_seats") + "," +
                                        resultSet.getInt("first_class_seats");
                String prices = resultSet.getDouble("economy_price") + "," +
                                resultSet.getDouble("business_price") + "," +
                                resultSet.getDouble("first_class_price");

                Flight flight = new Flight(flightNumber, airline, origin, destination, departureTime, arrivalTime, availableSeats, prices);
                flights.add(flight);
            }
            System.out.println("Flights loaded successfully.");
        } catch (SQLException e) {
            System.out.println("Error loading flights: " + e.getMessage());
        }
        return flights;
    }

    // Save bookings to the database
    public void saveBookings(List<Booking> bookings) {
        String sql = "INSERT INTO bookings (reference, customer_id, flight_id, status, payment_status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Booking booking : bookings) {
                statement.setString(1, booking.getBookingReference());
                statement.setString(2, booking.getCustomer());
                statement.setString(3, booking.getFlight());
                statement.setString(4, booking.getStatus());
                statement.setString(5, booking.getPaymentStatus());
                statement.addBatch();
            }
            statement.executeBatch();
            System.out.println("Bookings saved successfully.");
        } catch (SQLException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    // Load bookings from the database
    public List<Booking> loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String reference = resultSet.getString("reference");
                String customer = resultSet.getString("customer_id");
                String flight = resultSet.getString("flight_id");
                String status = resultSet.getString("status");
                String paymentStatus = resultSet.getString("payment_status");

                Booking booking = new Booking(reference, customer, flight, null, null, status, paymentStatus);
                bookings.add(booking);
            }
            System.out.println("Bookings loaded successfully.");
        } catch (SQLException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
        return bookings;
    }
    // In FileManager.java
public List<SystemSetting> loadSystemSettings() {
    List<SystemSetting> settings = new ArrayList<>();
    String sql = "SELECT setting_name, value FROM system_settings";
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
            String name = resultSet.getString("setting_name");
            String value = resultSet.getString("value");
            settings.add(new SystemSetting(name, value));
        }
        System.out.println("System settings loaded successfully.");
    } catch (SQLException e) {
        System.out.println("Error loading system settings: " + e.getMessage());
    }
    return settings;
}
}
