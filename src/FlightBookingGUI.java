import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import Models.*;
import db.*;

import static Models.User.resetPassword;

public class FlightBookingGUI extends JFrame {

    private static JFrame frame;
    private static CardLayout cardLayout;
    private static JPanel cards;
    private static User currentUser;
    private static final Color DARK_BLUE = Color.decode("#04009A");
    private static final Color LIGHT_BLUE = Color.decode("#77ACF1");
    private static final Color CYAN = Color.decode("#3EDBF0");
    private static final Color VERY_LIGHT_CYAN = Color.decode("#C0FEFC");

    public static void main(String[] args) {
        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Flight Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(createMainPanel(), "MAIN");
        cards.add(createCustomerPanel(), "CUSTOMER");
        cards.add(createAgentPanel(), "AGENT");
        cards.add(createAdminPanel(), "ADMIN");

        frame.add(cards);
        frame.setVisible(true);
    }

    private static JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(VERY_LIGHT_CYAN);

        JLabel titleLabel = new JLabel("Welcome to Flight Booking System!");
        titleLabel.setBounds(100, 50, 300, 30);
        titleLabel.setForeground(DARK_BLUE);
        panel.add(titleLabel);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 100, 200, 30);
        loginButton.setBackground(LIGHT_BLUE);
        loginButton.setForeground(DARK_BLUE);
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 150, 200, 30);
        registerButton.setBackground(LIGHT_BLUE);
        registerButton.setForeground(DARK_BLUE);
        panel.add(registerButton);

        // Add Reset Password Button
        JButton resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.setBounds(150, 200, 200, 30);
        resetPasswordButton.setBackground(LIGHT_BLUE);
        resetPasswordButton.setForeground(DARK_BLUE);
        panel.add(resetPasswordButton);

        loginButton.addActionListener(e -> showLoginDialog());
        registerButton.addActionListener(e -> showRegisterDialog());
        resetPasswordButton.addActionListener(e -> showResetPasswordDialog());

        return panel;
    }

    private static JPanel createCustomerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(CYAN);

        JLabel welcomeLabel = new JLabel("Welcome, Customer!");
        welcomeLabel.setBounds(100, 50, 300, 30);
        welcomeLabel.setForeground(DARK_BLUE);
        panel.add(welcomeLabel);

        JButton searchFlightsButton = new JButton("Search Flights");
        searchFlightsButton.setBounds(150, 100, 200, 30);
        searchFlightsButton.setBackground(LIGHT_BLUE);
        searchFlightsButton.setForeground(DARK_BLUE);
        panel.add(searchFlightsButton);

        JButton bookFlightButton = new JButton("Book a Flight");
        bookFlightButton.setBounds(150, 150, 200, 30);
        bookFlightButton.setBackground(LIGHT_BLUE);
        bookFlightButton.setForeground(DARK_BLUE);
        panel.add(bookFlightButton);

        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setBounds(150, 200, 200, 30);
        viewBookingsButton.setBackground(LIGHT_BLUE);
        viewBookingsButton.setForeground(DARK_BLUE);
        panel.add(viewBookingsButton);

        JButton cancelBookingButton = new JButton("Cancel Booking");
        cancelBookingButton.setBounds(150, 250, 200, 30);
        cancelBookingButton.setBackground(LIGHT_BLUE);
        cancelBookingButton.setForeground(DARK_BLUE);
        panel.add(cancelBookingButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 300, 200, 30);
        logoutButton.setBackground(LIGHT_BLUE);
        logoutButton.setForeground(DARK_BLUE);
        panel.add(logoutButton);

        searchFlightsButton.addActionListener(e -> searchFlights());
        bookFlightButton.addActionListener(e -> bookFlight());
        viewBookingsButton.addActionListener(e -> viewBookings());
        cancelBookingButton.addActionListener(e -> cancelBooking());
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(cards, "MAIN");
        });

        return panel;
    }

    private static JPanel createAgentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(LIGHT_BLUE);

        JLabel welcomeLabel = new JLabel("Welcome, Agent!");
        welcomeLabel.setBounds(100, 50, 300, 30);
        welcomeLabel.setForeground(DARK_BLUE);
        panel.add(welcomeLabel);

        JButton addFlightButton = new JButton("Add a Flight");
        addFlightButton.setBounds(150, 100, 200, 30);
        addFlightButton.setBackground(CYAN);
        addFlightButton.setForeground(DARK_BLUE);
        panel.add(addFlightButton);

        JButton viewFlightsButton = new JButton("View Flights");
        viewFlightsButton.setBounds(150, 150, 200, 30);
        viewFlightsButton.setBackground(CYAN);
        viewFlightsButton.setForeground(DARK_BLUE);
        panel.add(viewFlightsButton);

        JButton createBookingButton = new JButton("Create Booking");
        createBookingButton.setBounds(150, 200, 200, 30);
        createBookingButton.setBackground(CYAN);
        createBookingButton.setForeground(DARK_BLUE);
        panel.add(createBookingButton);

        JButton showReportsButton = new JButton("Show Reports");
        showReportsButton.setBounds(150, 250, 200, 30);
        showReportsButton.setBackground(CYAN);
        showReportsButton.setForeground(DARK_BLUE);
        panel.add(showReportsButton);

        JButton printTicketButton = new JButton("Print Ticket");
        printTicketButton.setBounds(150, 300, 200, 30);
        printTicketButton.setBackground(CYAN);
        printTicketButton.setForeground(DARK_BLUE);
        panel.add(printTicketButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 350, 200, 30);
        logoutButton.setBackground(CYAN);
        logoutButton.setForeground(DARK_BLUE);
        panel.add(logoutButton);

        addFlightButton.addActionListener(e -> addFlight());
        viewFlightsButton.addActionListener(e -> viewFlights());
        createBookingButton.addActionListener(e -> createBookingForCustomer());
        showReportsButton.addActionListener(e -> showFlightReports());
        printTicketButton.addActionListener(e -> printTicket());
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(cards, "MAIN");
        });

        return panel;
    }

    private static JPanel createAdminPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(DARK_BLUE);

        JLabel welcomeLabel = new JLabel("Welcome, Administrator!");
        welcomeLabel.setBounds(100, 20, 300, 30);
        welcomeLabel.setForeground(VERY_LIGHT_CYAN);
        panel.add(welcomeLabel);

        JButton createUserButton = new JButton("Create User");
        createUserButton.setBounds(150, 70, 200, 30);
        createUserButton.setBackground(LIGHT_BLUE);
        createUserButton.setForeground(DARK_BLUE);
        panel.add(createUserButton);

        JButton modifySettingsButton = new JButton("Modify System Settings");
        modifySettingsButton.setBounds(150, 120, 200, 30);
        modifySettingsButton.setBackground(LIGHT_BLUE);
        modifySettingsButton.setForeground(DARK_BLUE);
        panel.add(modifySettingsButton);

        JButton viewLogsButton = new JButton("View Logs");
        viewLogsButton.setBounds(150, 170, 200, 30);
        viewLogsButton.setBackground(LIGHT_BLUE);
        viewLogsButton.setForeground(DARK_BLUE);
        panel.add(viewLogsButton);

        JButton manageAccessButton = new JButton("Manage User Access");
        manageAccessButton.setBounds(150, 220, 200, 30);
        manageAccessButton.setBackground(LIGHT_BLUE);
        manageAccessButton.setForeground(DARK_BLUE);
        panel.add(manageAccessButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 270, 200, 30);
        logoutButton.setBackground(LIGHT_BLUE);
        logoutButton.setForeground(DARK_BLUE);
        panel.add(logoutButton);

        // Add action listeners for each button
        createUserButton.addActionListener(e -> showCreateUserPanel());
        modifySettingsButton.addActionListener(e -> showModifySettingsPanel());
        viewLogsButton.addActionListener(e -> showLogsPanel());
        manageAccessButton.addActionListener(e -> showManageAccessPanel());
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(cards, "MAIN");
        });

        return panel;
    }

    private static void showLoginDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                currentUser = User.login(username, password);
                if (currentUser != null) {
                    if (currentUser instanceof Customer) {
                        cardLayout.show(cards, "CUSTOMER");
                    } else if (currentUser instanceof Agent) {
                        cardLayout.show(cards, "AGENT");
                    } else if (currentUser instanceof Administrator) {
                        cardLayout.show(cards, "ADMIN");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Login error: " + e.getMessage());
            }
        }
    }

    private static void showRegisterDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField mobileField = new JTextField();

        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Name:", nameField,
                "Email:", emailField,
                "Mobile:", mobileField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String email = emailField.getText();
            String mobile = mobileField.getText();

            try {
                boolean success = User.register(username, password, name, email, mobile, "Customer");
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Registration successful! Please log in.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Registration failed.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Registration error: " + e.getMessage());
            }
        }
    }

    private static void bookFlight() {
    if (!(currentUser instanceof Customer)) return;
    Customer customer = (Customer) currentUser;

    String flightNumber = JOptionPane.showInputDialog(frame, "Enter Flight Number to book:");
    if (flightNumber == null || flightNumber.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Flight number cannot be empty.");
        return;
    }

    String seatClass = JOptionPane.showInputDialog(frame, "Enter seat class (economy, business, first class):");
    if (seatClass == null || seatClass.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Seat class cannot be empty.");
        return;
    }

    try {
        String flightId = getFlightIdByNumber(flightNumber);
        if (flightId == null) {
            JOptionPane.showMessageDialog(frame, "Flight not found.");
            return;
        }

        customer.createBooking(flightId, seatClass);
        JOptionPane.showMessageDialog(frame, "Booking successful!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(frame, "Booking error: " + e.getMessage());
    }
}

    private static String getFlightIdByNumber(String flightNumber) {
        String sql = "SELECT id FROM flights WHERE flight_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flightNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        }
        return null;
    }

    private static void viewBookings() {
        if (!(currentUser instanceof Customer)) return;
        Customer customer = (Customer) currentUser;

        try {
            List<Booking> bookings = customer.getBookings();
            if (bookings.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No bookings found.");
                return;
            }

            String[] columnNames = {"Reference", "Flight Number", "Status", "Payment Status"};
            Object[][] data = new Object[bookings.size()][columnNames.length];

            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                data[i][0] = booking.getBookingReference();
                data[i][1] = booking.getFlight();
                data[i][2] = booking.getStatus();
                data[i][3] = booking.getPaymentStatus();
            }

            showTable("View Bookings", columnNames, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error retrieving bookings: " + e.getMessage());
        }
    }
    private static void addFlight() {
        if (!(currentUser instanceof Agent)) return;
        Agent agent = (Agent) currentUser;

        // Create input fields
        JTextField flightNumberField = new JTextField();
        JTextField airlineField = new JTextField();
        JTextField originField = new JTextField();
        JTextField destinationField = new JTextField();
        JTextField departureTimeField = new JTextField();
        JTextField arrivalTimeField = new JTextField();
        JTextField economySeatsField = new JTextField();
        JTextField businessSeatsField = new JTextField();
        JTextField firstClassSeatsField = new JTextField();
        JTextField economyPriceField = new JTextField();
        JTextField businessPriceField = new JTextField();
        JTextField firstClassPriceField = new JTextField();

        // Create a panel to hold the input fields
        JPanel panel = new JPanel(new GridLayout(12, 2));
        panel.add(new JLabel("Flight Number:"));
        panel.add(flightNumberField);
        panel.add(new JLabel("Airline:"));
        panel.add(airlineField);
        panel.add(new JLabel("Origin:"));
        panel.add(originField);
        panel.add(new JLabel("Destination:"));
        panel.add(destinationField);
        panel.add(new JLabel("Departure Time (YYYY-MM-DD HH:MM):"));
        panel.add(departureTimeField);
        panel.add(new JLabel("Arrival Time (YYYY-MM-DD HH:MM):"));
        panel.add(arrivalTimeField);
        panel.add(new JLabel("Economy Seats:"));
        panel.add(economySeatsField);
        panel.add(new JLabel("Business Seats:"));
        panel.add(businessSeatsField);
        panel.add(new JLabel("First Class Seats:"));
        panel.add(firstClassSeatsField);
        panel.add(new JLabel("Economy Price:"));
        panel.add(economyPriceField);
        panel.add(new JLabel("Business Price:"));
        panel.add(businessPriceField);
        panel.add(new JLabel("First Class Price:"));
        panel.add(firstClassPriceField);

        // Show the dialog
        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Flight", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Parse input values
                String flightNumber = flightNumberField.getText();
                String airline = airlineField.getText();
                String origin = originField.getText();
                String destination = destinationField.getText();
                String departureTime = departureTimeField.getText();
                String arrivalTime = arrivalTimeField.getText();
                int economySeats = Integer.parseInt(economySeatsField.getText());
                int businessSeats = Integer.parseInt(businessSeatsField.getText());
                int firstClassSeats = Integer.parseInt(firstClassSeatsField.getText());
                double economyPrice = Double.parseDouble(economyPriceField.getText());
                double businessPrice = Double.parseDouble(businessPriceField.getText());
                double firstClassPrice = Double.parseDouble(firstClassPriceField.getText());

                // Call the agent's method to add the flight
                boolean success = agent.manageFlights(flightNumber, airline, origin, destination, departureTime, arrivalTime,
                        economySeats, businessSeats, firstClassSeats, economyPrice, businessPrice, firstClassPrice, "add");
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Flight added successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add flight.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input format. Please check your entries.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error adding flight: " + e.getMessage());
            }
        }
    }

    private static void viewFlights() {
        if (!(currentUser instanceof Agent)) return;
        Agent agent = (Agent) currentUser;

        try {
            List<Flight> flights = agent.getAllFlights();
            if (flights.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No flights found.");
                return;
            }

            String[] columnNames = {"Flight Number", "Airline", "Origin", "Destination", "Departure Time", "Arrival Time"};
            Object[][] data = new Object[flights.size()][columnNames.length];

            for (int i = 0; i < flights.size(); i++) {
                Flight flight = flights.get(i);
                data[i][0] = flight.getFlightNumber();
                data[i][1] = flight.getAirline();
                data[i][2] = flight.getOrigin();
                data[i][3] = flight.getDestination();
                data[i][4] = flight.getDepartureTime();
                data[i][5] = flight.getArrivalTime();
            }

            showTable("View Flights", columnNames, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error retrieving flights: " + e.getMessage());
        }
    }

    private static void manageUsers() {
        if (!(currentUser instanceof Administrator)) return;
        JOptionPane.showMessageDialog(frame, "Admin can manage users here.");
    }

    private static void viewAllBookings() {
        if (!(currentUser instanceof Administrator)) return;
        Administrator admin = (Administrator) currentUser;

        try {
            List<Booking> bookings = admin.getAllBookings();
            if (bookings.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No bookings found.");
                return;
            }

            String[] columnNames = {"Reference", "Customer ID", "Flight ID"};
            Object[][] data = new Object[bookings.size()][columnNames.length];

            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                data[i][0] = booking.getBookingReference();
                data[i][1] = booking.getCustomer();
                data[i][2] = booking.getFlight();
            }

            showTable("View All Bookings", columnNames, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error retrieving bookings: " + e.getMessage());
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

    private static void showFlightReports() {
        if (!(currentUser instanceof Agent)) return;
        Agent agent = (Agent) currentUser;

        try {
            String report = agent.generateReports();
            JOptionPane.showMessageDialog(frame, report.isEmpty() ? "No reports available." : report);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error generating reports: " + e.getMessage());
        }
    }

    private static void createBookingForCustomer() {
        if (!(currentUser instanceof Agent)) return;
        Agent agent = (Agent) currentUser;

        String customerId = JOptionPane.showInputDialog(frame, "Enter Customer ID:");
        if (customerId == null) return;

        String flightNumber = JOptionPane.showInputDialog(frame, "Enter Flight Number:");
        if (flightNumber == null) return;

        String seatClass = JOptionPane.showInputDialog(frame, "Enter Seat Class (economy, business, first class):");
        if (seatClass == null) return;

        String numPassengersStr = JOptionPane.showInputDialog(frame, "Enter Number of Passengers:");
        if (numPassengersStr == null) return;

        try {
            int numPassengers = Integer.parseInt(numPassengersStr);
            agent.createBookingForCustomer(customerId, flightNumber, seatClass, numPassengers);
            JOptionPane.showMessageDialog(frame, "Booking created successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid number of passengers.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error creating booking: " + e.getMessage());
        }
    }

    private static void showResetPasswordDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();

        Object[] message = {
            "Username:", usernameField,
            "Current Password:", currentPasswordField,
            "New Password:", newPasswordField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Reset Password", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());

            if (!validatePassword(newPassword)) {
                JOptionPane.showMessageDialog(frame, "New password must be at least 6 characters long and contain at least 1 number.");
                return;
            }

            try {
                boolean success = resetPassword(username, currentPassword, newPassword);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Password reset successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or current password.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error resetting password: " + e.getMessage());
            }
        }
    }

    private static boolean validatePassword(String password) {
        if (password.length() < 6) return false;
        return password.matches(".*\\d.*"); // Check if it contains at least one number
    }

    private static void showTable(String title, String[] columnNames, Object[][] data) {
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JFrame tableFrame = new JFrame(title);
        tableFrame.setSize(1000, 600);
        tableFrame.add(scrollPane);
        tableFrame.setVisible(true);
    }

    private static void searchFlights() {
        if (!(currentUser instanceof Customer)) return;
        Customer customer = (Customer) currentUser;

        String flightNumber = JOptionPane.showInputDialog(frame, "Enter Flight Number:");
        if (flightNumber == null || flightNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Flight number cannot be empty.");
            return;
        }

        try {
            List<Flight> flights = customer.searchFlights(flightNumber);
            if (flights.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No flights found for the given flight number.");
                return;
            }

            String[] columnNames = {"Flight Number", "Airline", "Origin", "Destination", "Departure Time", "Arrival Time"};
            Object[][] data = new Object[flights.size()][columnNames.length];

            for (int i = 0; i < flights.size(); i++) {
                Flight flight = flights.get(i);
                data[i][0] = flight.getFlightNumber();
                data[i][1] = flight.getAirline();
                data[i][2] = flight.getOrigin();
                data[i][3] = flight.getDestination();
                data[i][4] = flight.getDepartureTime();
                data[i][5] = flight.getArrivalTime();
            }

            showTable("Search Flights", columnNames, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error searching flights: " + e.getMessage());
        }
    }

    private static void cancelBooking() {
        if (!(currentUser instanceof Customer)) return;
        Customer customer = (Customer) currentUser;

        String bookingReference = JOptionPane.showInputDialog(frame, "Enter Booking Reference to cancel:");
        if (bookingReference == null || bookingReference.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Booking reference cannot be empty.");
            return;
        }

        try {
            boolean success = customer.cancelBooking(bookingReference);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Booking canceled successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to cancel booking. Please check the reference.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error canceling booking: " + e.getMessage());
        }
    }

    private static void showCreateUserPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("User ID:"));
        JTextField userIdField = new JTextField();
        panel.add(userIdField);

        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Role:"));
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Customer", "Agent", "Administrator"});
        panel.add(roleComboBox);

        JFrame createUserFrame = new JFrame("Create User");
        createUserFrame.setSize(400, 300);
        createUserFrame.add(panel);
        createUserFrame.setVisible(true);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String userId = userIdField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String email = emailField.getText();
            String role = (String) roleComboBox.getSelectedItem();

            Administrator admin = (Administrator) currentUser;
            boolean success = admin.createUser(userId, username, password, name, email, "", role);
            if (success) {
                JOptionPane.showMessageDialog(createUserFrame, "User created successfully!");
            } else {
                JOptionPane.showMessageDialog(createUserFrame, "Failed to create user.");
            }
        });
        panel.add(submitButton);
    }

    private static void showModifySettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Setting Name:"));
        JTextField settingNameField = new JTextField();
        panel.add(settingNameField);

        panel.add(new JLabel("New Value:"));
        JTextField newValueField = new JTextField();
        panel.add(newValueField);

        JFrame modifySettingsFrame = new JFrame("Modify System Settings");
        modifySettingsFrame.setSize(400, 200);
        modifySettingsFrame.add(panel);
        modifySettingsFrame.setVisible(true);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String settingName = settingNameField.getText();
            String newValue = newValueField.getText();

            if (settingName.isEmpty() || newValue.isEmpty()) {
                JOptionPane.showMessageDialog(modifySettingsFrame, "Setting name and value cannot be empty.");
                return;
            }

            // Update the setting in the database
            String sql = "UPDATE system_settings SET value = ? WHERE name = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newValue);
                stmt.setString(2, settingName);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(modifySettingsFrame, "Setting updated: " + settingName + " = " + newValue);
                } else {
                    JOptionPane.showMessageDialog(modifySettingsFrame, "Setting not found.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(modifySettingsFrame, "Error updating setting: " + ex.getMessage());
            }
        });
        panel.add(submitButton);
    }

    private static void showLogsPanel() {
        Administrator admin = (Administrator) currentUser;
        JFrame logsFrame = new JFrame("System Logs");
        logsFrame.setSize(800, 600);

        JTextArea logsArea = new JTextArea();
        logsArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(logsArea);
        logsFrame.add(scrollPane);

        logsFrame.setVisible(true);

        // Fetch logs and display them
        StringBuilder logs = new StringBuilder();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM system_logs ORDER BY log_date DESC");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                logs.append("Date: ").append(rs.getTimestamp("log_date"))
                    .append(", Action: ").append(rs.getString("action"))
                    .append(", Performed By: ").append(rs.getString("performed_by"))
                    .append("\n");
            }
        } catch (SQLException e) {
            logs.append("Error retrieving logs: ").append(e.getMessage());
        }
        logsArea.setText(logs.toString());
    }

    private static void showManageAccessPanel() {
        // Create the main panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add "User Name" label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("User Name:"), gbc);

        gbc.gridx = 1;
        JTextField userIdField = new JTextField(15);
        panel.add(userIdField, gbc);

        // Add "New Role" label and combo box
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("New Role:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Customer", "Agent", "Administrator"});
        panel.add(roleComboBox, gbc);

        // Add the Submit button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = new JButton("Submit");
        panel.add(submitButton, gbc);

        // Create and display the frame
        JFrame manageAccessFrame = new JFrame("Manage User Access");
        manageAccessFrame.setSize(400, 200);
        manageAccessFrame.add(panel);
        manageAccessFrame.setVisible(true);

        // Add action listener for the Submit button
        submitButton.addActionListener(e -> {
            String userId = userIdField.getText();
            String newRole = (String) roleComboBox.getSelectedItem();

            if (userId.isEmpty()) {
                JOptionPane.showMessageDialog(manageAccessFrame, "User Name cannot be empty.");
                return;
            }

            // Update the user's role in the database
            String sql = "UPDATE users SET role = ? WHERE username = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newRole);
                stmt.setString(2, userId);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(manageAccessFrame, "User access updated: " + userId + " -> " + newRole);
                } else {
                    JOptionPane.showMessageDialog(manageAccessFrame, "User not found.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(manageAccessFrame, "Error updating user access: " + ex.getMessage());
            }
        });
        panel.add(submitButton);
    }

    private static void printTicket() {
        if (!(currentUser instanceof Agent)) return;
        Agent agent = (Agent) currentUser;

        // Fetch bookings for the agent to select
        List<Booking> bookings = agent.getAllBookings();
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No bookings available to print tickets.");
            return;
        }

        // Create a table to display bookings
        String[] columnNames = {"Booking Reference", "Customer ID", "Flight ID", "Status"};
        Object[][] data = new Object[bookings.size()][columnNames.length];
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            data[i][0] = booking.getBookingReference();
            data[i][1] = booking.getCustomer();
            data[i][2] = booking.getFlight();
            data[i][3] = booking.getStatus();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Show the table in a dialog
        int result = JOptionPane.showConfirmDialog(frame, scrollPane, "Select a Booking to Print Ticket", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "No booking selected.");
                return;
            }

            // Get the selected booking reference
            String bookingReference = (String) data[selectedRow][0];
            Booking selectedBooking = bookings.stream()
                    .filter(b -> b.getBookingReference().equals(bookingReference))
                    .findFirst()
                    .orElse(null);

            if (selectedBooking == null) {
                JOptionPane.showMessageDialog(frame, "Invalid booking selected.");
                return;
            }

            // Fetch flight details for the selected booking
            Flight flight = agent.getFlightDetails(selectedBooking.getFlight());
            if (flight == null) {
                JOptionPane.showMessageDialog(frame, "Flight details not found for the selected booking.");
                return;
            }

            // Generate and display the ticket
            StringBuilder ticket = new StringBuilder();
            ticket.append("----- Flight Ticket -----\n")
                  .append("Booking Reference: ").append(selectedBooking.getBookingReference()).append("\n")
                  .append("Customer ID: ").append(selectedBooking.getCustomer()).append("\n")
                  .append("Flight Number: ").append(flight.getFlightNumber()).append("\n")
                  .append("Airline: ").append(flight.getAirline()).append("\n")
                  .append("Origin: ").append(flight.getOrigin()).append("\n")
                  .append("Destination: ").append(flight.getDestination()).append("\n")
                  .append("Departure Time: ").append(flight.getDepartureTime()).append("\n")
                  .append("Arrival Time: ").append(flight.getArrivalTime()).append("\n")
                  .append("-------------------------");

            JOptionPane.showMessageDialog(frame, ticket.toString(), "Flight Ticket", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
