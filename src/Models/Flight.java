package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DBConnection;

public class Flight {
    private String flightNumber, airline, origin, destination, departureTime, arrivalTime, availableSeats, prices;

    public Flight(String flightNumber, String airline, String origin, String destination, String departureTime, String arrivalTime, String availableSeats, String prices) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.prices = prices;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
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

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public void checkAvailability() {
        String sql = "SELECT economy_seats, business_seats, first_class_seats FROM flights WHERE flight_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.flightNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Economy Seats: " + rs.getInt("economy_seats"));
                System.out.println("Business Seats: " + rs.getInt("business_seats"));
                System.out.println("First Class Seats: " + rs.getInt("first_class_seats"));
            } else {
                System.out.println("Flight not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error checking availability: " + e.getMessage());
        }
    }

    public void updateSchedule(String newDepartureTime, String newArrivalTime) {
        String sql = "UPDATE flights SET departure_time = ?, arrival_time = ? WHERE flight_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newDepartureTime);
            stmt.setString(2, newArrivalTime);
            stmt.setString(3, this.flightNumber);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                this.departureTime = newDepartureTime;
                this.arrivalTime = newArrivalTime;
                System.out.println("Schedule updated successfully.");
            } else {
                System.out.println("Flight not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating schedule: " + e.getMessage());
        }
    }

    public void calculatePrice(String seatClass) {
        String sql = "SELECT economy_price, business_price, first_class_price FROM flights WHERE flight_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.flightNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double price = 0.0;
                switch (seatClass.toLowerCase()) {
                    case "economy":
                        price = rs.getDouble("economy_price");
                        break;
                    case "business":
                        price = rs.getDouble("business_price");
                        break;
                    case "first class":
                        price = rs.getDouble("first_class_price");
                        break;
                    default:
                        System.out.println("Invalid seat class.");
                        return;
                }
                System.out.println("Price for " + seatClass + " class: $" + price);
            } else {
                System.out.println("Flight not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating price: " + e.getMessage());
        }
    }

    public void reserveSeat(String seatClass, int numberOfSeats) {
        String sql = "UPDATE flights SET " + seatClass.toLowerCase() + "_seats = " + seatClass.toLowerCase() + "_seats - ? WHERE flight_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numberOfSeats);
            stmt.setString(2, this.flightNumber);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(numberOfSeats + " " + seatClass + " seats reserved successfully.");
            } else {
                System.out.println("Flight not found or insufficient seats available.");
            }
        } catch (SQLException e) {
            System.out.println("Error reserving seat: " + e.getMessage());
        }
    }
}
