package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                             String departureTime, String arrivalTime, int economySeats, int businessSeats,
                             int firstClassSeats, double economyPrice, double businessPrice, double firstClassPrice, String action) {
        if (flightNumber == null || flightNumber.isEmpty() || airline == null || airline.isEmpty()) {
            System.out.println("Invalid flight details provided.");
            return false;
        }

        String sql;
        if ("add".equalsIgnoreCase(action)) {
            sql = "INSERT INTO flights (flight_number, airline, origin, destination, departure_time, arrival_time, " +
                  "economy_seats, business_seats, first_class_seats, economy_price, business_price, first_class_price) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else if ("update".equalsIgnoreCase(action)) {
            sql = "UPDATE flights SET airline = ?, origin = ?, destination = ?, departure_time = ?, arrival_time = ?, " +
                  "economy_seats = ?, business_seats = ?, first_class_seats = ?, economy_price = ?, business_price = ?, " +
                  "first_class_price = ? WHERE flight_number = ?";
        } else if ("delete".equalsIgnoreCase(action)) {
            sql = "DELETE FROM flights WHERE flight_number = ?";
        } else {
            System.out.println("Invalid action: " + action);
            return false;
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
                stmt.setInt(7, economySeats);
                stmt.setInt(8, businessSeats);
                stmt.setInt(9, firstClassSeats);
                stmt.setDouble(10, economyPrice);
                stmt.setDouble(11, businessPrice);
                stmt.setDouble(12, firstClassPrice);
            } else if ("update".equalsIgnoreCase(action)) {
                stmt.setString(1, airline);
                stmt.setString(2, origin);
                stmt.setString(3, destination);
                stmt.setString(4, departureTime);
                stmt.setString(5, arrivalTime);
                stmt.setInt(6, economySeats);
                stmt.setInt(7, businessSeats);
                stmt.setInt(8, firstClassSeats);
                stmt.setDouble(9, economyPrice);
                stmt.setDouble(10, businessPrice);
                stmt.setDouble(11, firstClassPrice);
                stmt.setString(12, flightNumber);
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

    public void createBookingForCustomer(String customerId, String flightNumber, String seatClass, int numberOfPassengers) {
        String flightId = getFlightIdByNumber(flightNumber);
        if (flightId == null) {
            System.out.println("Flight not found: " + flightNumber);
            return;
        }

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

    public String generateReports() {
        StringBuilder report = new StringBuilder();
        String sql = "SELECT flight_id, COUNT(*) AS total_bookings FROM bookings GROUP BY flight_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                report.append("Flight ID: ").append(rs.getString("flight_id"))
                      .append(", Total Bookings: ").append(rs.getInt("total_bookings"))
                      .append("\n");
            }
        } catch (SQLException e) {
            System.out.println("Error generating reports: " + e.getMessage());
        }
        return report.toString();
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
        }
        return false;
    }

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String flightNumber = rs.getString("flight_number");
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
            System.out.println("Error retrieving flights: " + e.getMessage());
        }
        return flights;
    }

    public Flight getFlightDetails(String flightId) {
        String sql = "SELECT * FROM flights WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flightId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String flightNumber = rs.getString("flight_number");
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

                return new Flight(flightNumber, airline, origin, destination, departureTime, arrivalTime, availableSeats, prices);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving flight details: " + e.getMessage());
        }
        return null;
    }

    private String getFlightIdByNumber(String flightNumber) {
        String sql = "SELECT id FROM flights WHERE flight_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flightNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving flight ID: " + e.getMessage());
        }
        return null;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String bookingReference = rs.getString("reference");
                String customer = rs.getString("customer_id");
                String flight = rs.getString("flight_id");
                String status = rs.getString("status");
                bookings.add(new Booking(bookingReference, customer, flight, null, null, status, null));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving bookings: " + e.getMessage());
        }
        return bookings;
    }
}
