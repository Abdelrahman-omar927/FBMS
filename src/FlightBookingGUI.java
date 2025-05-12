import Models.*;
import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FlightBookingGUI {
    private static User currentUser;

    public static void main(String[] args) {
        

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                try (Connection connection = DBConnection.getConnection()) {
                    if (connection != null) {
                        System.out.println("Connection to SQLite has been established.");
                     }
                } catch (SQLException e) {
                         System.out.println("Connection failed: " + e.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            showLoginWindow();
        });
    }

    private static void showLoginWindow() {
        JFrame frame = new JFrame("Flight Booking System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(200, 200, 200, 200));
        panel.setBackground(Color.decode("#04009A")); // Set background color

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.decode("#C0FEFC")); // Set text color
        JTextField txtUsername = new JTextField();
        txtUsername.setBackground(Color.decode("#C0FEFC")); // Set text field background
        txtUsername.setForeground(Color.decode("#04009A")); // Set text field text color

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.decode("#C0FEFC")); // Set text color
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBackground(Color.decode("#C0FEFC")); // Set text field background
        txtPassword.setForeground(Color.decode("#04009A")); // Set text field text color

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        JButton btnChangePassword = new JButton("Change Password");

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnRegister);
        panel.add(btnChangePassword);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            currentUser = User.login(username, password);
            if (currentUser != null) {
                frame.dispose();
                showRoleDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials!");
            }
        });

        btnRegister.addActionListener(e -> showRegistrationDialog(frame));

        btnChangePassword.addActionListener(e -> showChangePasswordDialog(frame));

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void showRegistrationDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Register", true);
        dialog.setSize(1000, 600);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.decode("#04009A")); // Set background color
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // User ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUserId = new JLabel("User ID:");
        lblUserId.setForeground(Color.decode("#C0FEFC")); // Set text color
        panel.add(lblUserId, gbc);

        gbc.gridx = 1;
        JTextField txtUserId = new JTextField(20);
        txtUserId.setBackground(Color.decode("#C0FEFC")); // Set text field background
        txtUserId.setForeground(Color.decode("#04009A")); // Set text field text color
        panel.add(txtUserId, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.decode("#C0FEFC")); // Set text color
        panel.add(lblUsername, gbc);

        gbc.gridx = 1;
        JTextField txtUsername = new JTextField(20);
        txtUsername.setBackground(Color.decode("#C0FEFC")); // Set text field background
        txtUsername.setForeground(Color.decode("#04009A")); // Set text field text color
        panel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.decode("#C0FEFC")); // Set text color
        panel.add(lblPassword, gbc);

        gbc.gridx = 1;
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setBackground(Color.decode("#C0FEFC")); // Set text field background
        txtPassword.setForeground(Color.decode("#04009A")); // Set text field text color
        panel.add(txtPassword, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(Color.decode("#C0FEFC")); // Set text color
        panel.add(lblName, gbc);

        gbc.gridx = 1;
        JTextField txtName = new JTextField(20);
        txtName.setBackground(Color.decode("#C0FEFC")); // Set text field background
        txtName.setForeground(Color.decode("#04009A")); // Set text field text color
        panel.add(txtName, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.decode("#C0FEFC")); // Set text color
        panel.add(lblEmail, gbc);

        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(20);
        txtEmail.setBackground(Color.decode("#C0FEFC")); // Set text field background
        txtEmail.setForeground(Color.decode("#04009A")); // Set text field text color
        panel.add(txtEmail, gbc);

        // Contact
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblContact = new JLabel("Contact:");
        lblContact.setForeground(Color.decode("#C0FEFC")); // Set text color
        panel.add(lblContact, gbc);

        gbc.gridx = 1;
        JTextField txtContact = new JTextField(20);
        txtContact.setBackground(Color.decode("#C0FEFC")); // Set text field background
        txtContact.setForeground(Color.decode("#04009A")); // Set text field text color
        panel.add(txtContact, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel lblRole = new JLabel("Role:");
        lblRole.setForeground(Color.decode("#C0FEFC")); // Set text color
        panel.add(lblRole, gbc);

        gbc.gridx = 1;
        String[] roles = {"Customer", "Agent", "Administrator"};
        JComboBox<String> cmbRole = new JComboBox<>(roles);
        cmbRole.setBackground(Color.decode("#C0FEFC")); // Set combo box background
        cmbRole.setForeground(Color.decode("#04009A")); // Set combo box text color
        panel.add(cmbRole, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnRegister = new JButton("Register");
        btnRegister.setBackground(Color.decode("#C0FEFC")); // Set button background
        btnRegister.setForeground(Color.decode("#04009A")); // Set button text color
        btnRegister.addActionListener(e -> {
            String userId = txtUserId.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String contact = txtContact.getText().trim();
            String role = cmbRole.getSelectedItem().toString();

            // Validate input
            if (userId.isEmpty() || username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                showError("All fields are required.");
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                showError("Invalid email format.");
                return;
            }


            boolean success = User.register(
                    txtUsername.getText().trim(),
                    new String(txtPassword.getPassword()).trim(),
                    txtName.getText().trim(),
                    txtEmail.getText().trim(),
                    txtContact.getText().trim(),
                    cmbRole.getSelectedItem().toString()
            );
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Registration successful!");
                dialog.dispose();
            } else {
                showError("Registration failed.");
            }
        });
        panel.add(btnRegister, gbc);

        dialog.add(panel);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static void showRoleDashboard() {
        if (currentUser == null) return;

        JFrame frame = new JFrame("Flight Booking System - Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        if (currentUser instanceof Administrator) {
            tabbedPane.addTab("User Management", createAdminUserPanel());
            tabbedPane.addTab("System Settings", createSystemSettingsPanel());
        } else if (currentUser instanceof Agent) {
            tabbedPane.addTab("Flight Management", createAgentFlightPanel());
            tabbedPane.addTab("Bookings", createAgentBookingPanel());
        } else if (currentUser instanceof Customer) {
            tabbedPane.addTab("Search Flights", createCustomerSearchPanel());
            tabbedPane.addTab("My Bookings", createCustomerBookingsPanel());
        }

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    // ADMIN PANELS
    private static JPanel createAdminUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table showing users
        JTable userTable = new JTable(getUserTableModel());
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Toolbar
        JToolBar toolbar = new JToolBar();
        JButton btnAdd = new JButton("Add User");
        JButton btnEdit = new JButton("Edit");

        btnAdd.addActionListener(e -> {
            JTextField txtUserId = new JTextField();
            JTextField txtUsername = new JTextField();
            JTextField txtPassword = new JTextField();
            JTextField txtName = new JTextField();
            JTextField txtEmail = new JTextField();
            JTextField txtContact = new JTextField();
            JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Customer", "Agent", "Administrator"});

            Object[] message = {
                "User ID:", txtUserId,
                "Username:", txtUsername,
                "Password:", txtPassword,
                "Name:", txtName,
                "Email:", txtEmail,
                "Contact:", txtContact,
                "Role:", cmbRole
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                Administrator admin = (Administrator) currentUser;
                boolean success = admin.createUser(
                    txtUserId.getText().trim(),
                    txtUsername.getText().trim(),
                    txtPassword.getText().trim(),
                    txtName.getText().trim(),
                    txtEmail.getText().trim(),
                    txtContact.getText().trim(),
                    cmbRole.getSelectedItem().toString()
                );
                if (success) {
                    userTable.setModel(getUserTableModel());
                    showSuccess("User added successfully!");
                } else {
                    showError("Failed to add user. Please check the input.");
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                String userId = (String) userTable.getValueAt(selectedRow, 0);
                String username = (String) userTable.getValueAt(selectedRow, 1);
                String name = (String) userTable.getValueAt(selectedRow, 2);
                String role = (String) userTable.getValueAt(selectedRow, 3);

                JTextField txtUsername = new JTextField(username);
                JTextField txtName = new JTextField(name);
                JTextField txtEmail = new JTextField();
                JTextField txtContact = new JTextField();
                JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Customer", "Agent", "Administrator"});
                cmbRole.setSelectedItem(role);

                Object[] message = {
                    "Username:", txtUsername,
                    "Name:", txtName,
                    "Email:", txtEmail,
                    "Contact:", txtContact,
                    "Role:", cmbRole
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Edit User", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    Administrator admin = (Administrator) currentUser;
                    boolean success = admin.updateUser(
                        userId,
                        txtUsername.getText().trim(),
                        txtName.getText().trim(),
                        txtEmail.getText().trim(),
                        txtContact.getText().trim(),
                        cmbRole.getSelectedItem().toString()
                    );
                    if (success) {
                        userTable.setModel(getUserTableModel());
                        showSuccess("User updated successfully!");
                    } else {
                        showError("Failed to update user.");
                    }
                }
            } else {
                showError("Please select a user to edit.");
            }
        });

        toolbar.add(btnAdd);
        toolbar.add(btnEdit);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel createSystemSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTable settingsTable = new JTable(getSystemSettingsModel());
        JScrollPane scrollPane = new JScrollPane(settingsTable);

        JToolBar toolbar = new JToolBar();
        JButton btnEdit = new JButton("Edit Setting");

        btnEdit.addActionListener(e -> {
            int selectedRow = settingsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String settingName = (String) settingsTable.getValueAt(selectedRow, 0);
                JTextField txtValue = new JTextField((String) settingsTable.getValueAt(selectedRow, 1));
                Object[] message = {
                    "New Value:", txtValue
                };
                int option = JOptionPane.showConfirmDialog(null, message, "Edit Setting", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    Administrator admin = (Administrator) currentUser;
                    admin.modifySystemSettings(settingName, txtValue.getText());
                    settingsTable.setModel(getSystemSettingsModel());
                }
            } else {
                showError("Please select a setting to edit.");
            }
        });

        toolbar.add(btnEdit);
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // AGENT PANELS
    private static JPanel createAgentFlightPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTable flightTable = new JTable(getFlightTableModel());
        JScrollPane scrollPane = new JScrollPane(flightTable);

        JToolBar toolbar = new JToolBar();
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnAdd = new JButton("Add Flight");
        toolbar.add(btnAdd);

        btnAdd.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) null, "Add Flight", true);
            dialog.setSize(400, 400);
            dialog.setLocationRelativeTo(null);

            JPanel panel1 = new JPanel(new GridLayout(8, 2, 10, 10));
            panel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JTextField txtFlightNumber = new JTextField();
            JTextField txtAirline = new JTextField();
            JTextField txtOrigin = new JTextField();
            JTextField txtDestination = new JTextField();
            JTextField txtDepartureTime = new JTextField();
            JTextField txtArrivalTime = new JTextField();
            JTextField txtPrice = new JTextField();

            panel1.add(new JLabel("Flight Number:"));
            panel1.add(txtFlightNumber);
            panel1.add(new JLabel("Airline:"));
            panel1.add(txtAirline);
            panel1.add(new JLabel("Origin:"));
            panel1.add(txtOrigin);
            panel1.add(new JLabel("Destination:"));
            panel1.add(txtDestination);
            panel1.add(new JLabel("Departure Time:"));
            panel1.add(txtDepartureTime);
            panel1.add(new JLabel("Arrival Time:"));
            panel1.add(txtArrivalTime);
            panel1.add(new JLabel("Price (Economy):"));
            panel1.add(txtPrice);

            JButton btnSave = new JButton("Save");
            btnSave.addActionListener(event -> {
                String flightNumber = txtFlightNumber.getText().trim();
                String airline = txtAirline.getText().trim();
                String origin = txtOrigin.getText().trim();
                String destination = txtDestination.getText().trim();
                String departureTime = txtDepartureTime.getText().trim();
                String arrivalTime = txtArrivalTime.getText().trim();
                String price = txtPrice.getText().trim();

                // Validate input
                if (flightNumber.isEmpty() || airline.isEmpty() || origin.isEmpty() || destination.isEmpty() ||
                    departureTime.isEmpty() || arrivalTime.isEmpty() || price.isEmpty()) {
                    showError("All fields are required.");
                    return;
                }

                try {
                    double economyPrice = Double.parseDouble(price);

                    // Add flight to the database
                    Agent agent = (Agent) currentUser;
                    boolean success = agent.manageFlights(
                        flightNumber, airline, origin, destination, departureTime, arrivalTime, economyPrice, "add"
                    );

                    if (success) {
                        // Refresh the flight table
                        flightTable.setModel(getFlightTableModel());
                        showSuccess("Flight added successfully!");
                        dialog.dispose();
                    } else {
                        showError("Failed to add flight. Please check the input.");
                    }
                } catch (NumberFormatException ex) {
                    showError("Invalid price format. Please enter a valid number.");
                }
            });

            panel1.add(btnSave);
            dialog.add(panel1);
            dialog.setVisible(true);
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = flightTable.getSelectedRow();
            if (selectedRow >= 0) {
                String flightId = (String) flightTable.getValueAt(selectedRow, 0);
                Flight flight = getFlightById(flightId);
                showFlightDialog("Edit Flight", flight, flightTable);
            } else {
                showError("Please select a flight to edit.");
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = flightTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(
                    panel,
                    "Are you sure you want to delete this flight?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    String flightNumber = (String) flightTable.getValueAt(selectedRow, 0);
                    Agent agent = (Agent) currentUser;
                    boolean success = agent.manageFlights(flightNumber, null, null, null, null, null, 0, "delete");
                    if (success) {
                        flightTable.setModel(getFlightTableModel());
                        showSuccess("Flight deleted successfully.");
                    } else {
                        showError("Failed to delete flight. Please try again.");
                    }
                }
            } else {
                showError("Please select a flight to delete.");
            }
        });

        toolbar.add(btnEdit);
        toolbar.add(btnDelete);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel createAgentBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTable bookingsTable = new JTable(getAgentBookingsModel());
        JScrollPane scrollPane = new JScrollPane(bookingsTable);

        JToolBar toolbar = new JToolBar();
        JButton btnCancelBooking = new JButton("Cancel Booking");
        JButton btnConfirmPayment = new JButton("Confirm Payment");

        btnCancelBooking.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String bookingReference = (String) bookingsTable.getValueAt(selectedRow, 0);
                Agent agent = (Agent) currentUser;
                boolean success = agent.cancelBooking(bookingReference);
                if (success) {
                    bookingsTable.setModel(getAgentBookingsModel());
                    showSuccess("Booking canceled successfully.");
                } else {
                    showError("Failed to cancel booking.");
                }
            } else {
                showError("Please select a booking to cancel.");
            }
        });

        btnConfirmPayment.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String bookingReference = (String) bookingsTable.getValueAt(selectedRow, 0);
                String paymentStatus = (String) bookingsTable.getValueAt(selectedRow, 4);

                if ("Pending".equalsIgnoreCase(paymentStatus)) {
                    String[] paymentMethods = {"Credit Card", "Bank Transfer"};
                    String selectedMethod = (String) JOptionPane.showInputDialog(
                        panel,
                        "Select Payment Method:",
                        "Payment Confirmation",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        paymentMethods,
                        paymentMethods[0]
                    );

                    if (selectedMethod != null) {
                        // Get flight price from database
                        String flightNumber = (String) bookingsTable.getValueAt(selectedRow, 1);
                        String sql = "SELECT f.economy_price FROM flights f " +
                                   "JOIN bookings b ON b.flight_id = f.id " +
                                   "WHERE b.reference = ?";
                        try (Connection conn = DBConnection.getConnection();
                             PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setString(1, bookingReference);
                            ResultSet rs = stmt.executeQuery();
                            if (rs.next()) {
                                double price = rs.getDouble("economy_price");
                                Payment payment = new Payment(
                                    "PAY" + System.currentTimeMillis(),
                                    bookingReference,
                                    String.format("%.2f", price),
                                    selectedMethod,
                                    "Completed",
                                    java.time.LocalDate.now().toString()
                                );
                                payment.processPayment();
                                bookingsTable.setModel(getAgentBookingsModel());
                                showSuccess("Payment confirmed successfully!");
                            } else {
                                showError("Could not find flight price for this booking.");
                            }
                        } catch (SQLException ex) {
                            showError("Error processing payment: " + ex.getMessage());
                        }
                    }
                } else {
                    showError("Payment is already completed for this booking.");
                }
            } else {
                showError("Please select a booking to confirm payment.");
            }
        });

        toolbar.add(btnCancelBooking);
        toolbar.add(btnConfirmPayment);
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // CUSTOMER PANELS
    private static JPanel createCustomerSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtFlightNumber = new JTextField();
        JButton btnSearch = new JButton("Search");
        JButton btnBook = new JButton("Book Flight");

        searchPanel.add(new JLabel("Flight Number:"));
        searchPanel.add(txtFlightNumber);
        searchPanel.add(btnSearch);

        JTable resultsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultsTable);

        btnSearch.addActionListener(e -> {
            String flightNumber = txtFlightNumber.getText().trim();
            if (flightNumber.isEmpty()) {
                showError("Please enter a flight number.");
                return;
            }

            Customer customer = (Customer) currentUser;
            List<Flight> flights = customer.searchFlights(flightNumber);
            resultsTable.setModel(getFlightResultsModel(flights));
        });

        btnBook.addActionListener(e -> {
            int selectedRow = resultsTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Retrieve the flight number from the selected row
                String flightNumber = (String) resultsTable.getValueAt(selectedRow, 0);
                Customer customer = (Customer) currentUser;

                // Book the flight
                boolean success = customer.bookFlight(flightNumber);
                if (success) {
                    // Refresh the "My Bookings" table
                    JTabbedPane parentTabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, panel);
                    if (parentTabbedPane != null) {
                        int bookingsTabIndex = parentTabbedPane.indexOfTab("My Bookings");
                        if (bookingsTabIndex != -1) {
                            JPanel bookingsPanel = (JPanel) parentTabbedPane.getComponentAt(bookingsTabIndex);
                            JTable bookingsTable = (JTable) ((JScrollPane) bookingsPanel.getComponent(1)).getViewport().getView();
                            bookingsTable.setModel(getCustomerBookingsModel());
                        }
                    }
                }
                // Removed pop-up messages (showSuccess and showError)
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnBook);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static JPanel createCustomerBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTable bookingsTable = new JTable(getCustomerBookingsModel());
        JScrollPane scrollPane = new JScrollPane(bookingsTable);

        JToolBar toolbar = new JToolBar();
        JButton btnCancel = new JButton("Cancel Booking");
        JButton btnConfirmPayment = new JButton("Confirm Payment");

        btnCancel.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String bookingReference = (String) bookingsTable.getValueAt(selectedRow, 0);
                Customer customer = (Customer) currentUser;
                customer.cancelBooking(bookingReference);
                bookingsTable.setModel(getCustomerBookingsModel());
                showSuccess("Booking canceled successfully.");
            } else {
                showError("Please select a booking to cancel.");
            }
        });

        btnConfirmPayment.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String bookingReference = (String) bookingsTable.getValueAt(selectedRow, 0);
                String paymentStatus = (String) bookingsTable.getValueAt(selectedRow, 3);

                if ("Pending".equalsIgnoreCase(paymentStatus)) {
                    String[] paymentMethods = {"Credit Card", "Bank Transfer"};
                    String selectedMethod = (String) JOptionPane.showInputDialog(
                        panel,
                        "Select Payment Method:",
                        "Payment Confirmation",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        paymentMethods,
                        paymentMethods[0]
                    );

                    if (selectedMethod != null) {
                        // Get flight price from database
                        String flightNumber = (String) bookingsTable.getValueAt(selectedRow, 1);
                        String sql = "SELECT f.economy_price FROM flights f " +
                                   "JOIN bookings b ON b.flight_id = f.id " +
                                   "WHERE b.reference = ?";
                        try (Connection conn = DBConnection.getConnection();
                             PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setString(1, bookingReference);
                            ResultSet rs = stmt.executeQuery();
                            if (rs.next()) {
                                double price = rs.getDouble("economy_price");
                                
                                // Create a dialog for payment details
                                JDialog paymentDialog = new JDialog((Frame) null, "Payment Details", true);
                                paymentDialog.setSize(400, 300);
                                paymentDialog.setLocationRelativeTo(null);

                                JPanel paymentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                                paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                                // Add price display
                                paymentPanel.add(new JLabel("Amount to Pay:"));
                                paymentPanel.add(new JLabel(String.format("$%.2f", price)));

                                if ("Credit Card".equals(selectedMethod)) {
                                    // Credit Card fields
                                    JTextField cardNumberField = new JTextField();
                                    JTextField expiryDateField = new JTextField();
                                    JTextField cvvField = new JTextField();
                                    JTextField cardHolderField = new JTextField();

                                    paymentPanel.add(new JLabel("Card Number:"));
                                    paymentPanel.add(cardNumberField);
                                    paymentPanel.add(new JLabel("Expiry Date (MM/YY):"));
                                    paymentPanel.add(expiryDateField);
                                    paymentPanel.add(new JLabel("CVV:"));
                                    paymentPanel.add(cvvField);
                                    paymentPanel.add(new JLabel("Card Holder Name:"));
                                    paymentPanel.add(cardHolderField);

                                    JButton confirmButton = new JButton("Confirm Payment");
                                    confirmButton.addActionListener(event -> {
                                        // Validate credit card details
                                        if (cardNumberField.getText().trim().isEmpty() ||
                                            expiryDateField.getText().trim().isEmpty() ||
                                            cvvField.getText().trim().isEmpty() ||
                                            cardHolderField.getText().trim().isEmpty()) {
                                            showError("All credit card fields are required.");
                                            return;
                                        }

                                        // Process payment with credit card details
                                        Payment payment = new Payment(
                                            "PAY" + System.currentTimeMillis(),
                                            bookingReference,
                                            String.format("%.2f", price),
                                            selectedMethod,
                                            "Completed",
                                            java.time.LocalDate.now().toString()
                                        );
                                        payment.processPayment();
                                        bookingsTable.setModel(getCustomerBookingsModel());
                                        showSuccess("Payment confirmed successfully!");
                                        paymentDialog.dispose();
                                    });
                                    paymentPanel.add(confirmButton);

                                } else if ("Bank Transfer".equals(selectedMethod)) {
                                    // Bank Transfer fields
                                    JTextField accountNumberField = new JTextField();
                                    JTextField bankNameField = new JTextField();
                                    JTextField routingNumberField = new JTextField();

                                    paymentPanel.add(new JLabel("Account Number:"));
                                    paymentPanel.add(accountNumberField);
                                    paymentPanel.add(new JLabel("Bank Name:"));
                                    paymentPanel.add(bankNameField);
                                    paymentPanel.add(new JLabel("Routing Number:"));
                                    paymentPanel.add(routingNumberField);

                                    JButton confirmButton = new JButton("Confirm Payment");
                                    confirmButton.addActionListener(event -> {
                                        // Validate bank transfer details
                                        if (accountNumberField.getText().trim().isEmpty() ||
                                            bankNameField.getText().trim().isEmpty() ||
                                            routingNumberField.getText().trim().isEmpty()) {
                                            showError("All bank transfer fields are required.");
                                            return;
                                        }

                                        // Process payment with bank transfer details
                                        Payment payment = new Payment(
                                            "PAY" + System.currentTimeMillis(),
                                            bookingReference,
                                            String.format("%.2f", price),
                                            selectedMethod,
                                            "Completed",
                                            java.time.LocalDate.now().toString()
                                        );
                                        payment.processPayment();
                                        bookingsTable.setModel(getCustomerBookingsModel());
                                        showSuccess("Payment confirmed successfully!");
                                        paymentDialog.dispose();
                                    });
                                    paymentPanel.add(confirmButton);
                                }

                                paymentDialog.add(paymentPanel);
                                paymentDialog.setVisible(true);
                            } else {
                                showError("Could not find flight price for this booking.");
                            }
                        } catch (SQLException ex) {
                            showError("Error processing payment: " + ex.getMessage());
                        }
                    }
                } else {
                    showError("Payment is already completed for this booking.");
                }
            } else {
                showError("Please select a booking to confirm payment.");
            }
        });

        toolbar.add(btnCancel);
        toolbar.add(btnConfirmPayment);
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // UTILITY METHODS
    private static javax.swing.table.DefaultTableModel getUserTableModel() {
        String[] columns = {"User ID", "Username", "Name", "Role", "Status"};
        List<User> users = new FileManager().loadUsers();

        Object[][] data = new Object[users.size()][5];
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            String status = getUserStatus(u.getUserId());
            data[i] = new Object[]{
                u.getUserId(),
                u.getUsername(),
                u.getName(),
                u.getRole(),
                status
            };
        }

        return new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static String getUserStatus(String userId) {
        String sql = "SELECT status FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user status: " + e.getMessage());
        }
        return "Unknown";
    }

    private static javax.swing.table.DefaultTableModel getSystemSettingsModel() {
        String[] columns = {"Setting Name", "Value"};
        List<SystemSetting> settings = new FileManager().loadSystemSettings();

        Object[][] data = new Object[settings.size()][2];
        for (int i = 0; i < settings.size(); i++) {
            SystemSetting s = settings.get(i);
            data[i] = new Object[]{
                s.getName(),
                s.getValue()
            };
        }

        return new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static TableModel getFlightTableModel() {
        String[] columnNames = {"Flight Number", "Airline", "Origin", "Destination", "Departure Time", "Arrival Time", "Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT flight_number, airline, origin, destination, departure_time, arrival_time, economy_price FROM flights";

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
                double price = rs.getDouble("economy_price");

                model.addRow(new Object[]{flightNumber, airline, origin, destination, departureTime, arrivalTime, price});
            }
        } catch (SQLException e) {
            System.out.println("Error fetching flight data: " + e.getMessage());
        }

        return model;
    }

    private static javax.swing.table.DefaultTableModel getFlightResultsModel(List<Flight> flights) {
        String[] columns = {"Flight Number", "Airline", "Origin", "Destination", "Departure Time", "Arrival Time", "Price"};

        Object[][] data = new Object[flights.size()][7];
        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            data[i] = new Object[]{
                f.getFlightNumber(),
                f.getAirline(),
                f.getOrigin(),
                f.getDestination(),
                f.getDepartureTime(),
                f.getArrivalTime(),
                f.getPrices().split(",")[0] // Show economy price
            };
        }

        return new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static DefaultTableModel getCustomerBookingsModel() {
        String[] columns = {"Booking Reference", "Flight Number", "Status", "Payment Status"};
        Customer customer = (Customer) currentUser;
        List<Booking> bookings = customer.getBookings();

        Object[][] data = new Object[bookings.size()][4];
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            data[i][0] = booking.getBookingReference();
            data[i][1] = booking.getFlight();
            data[i][2] = booking.getStatus();
            data[i][3] = booking.getPaymentStatus();
        }

        return new DefaultTableModel(data, columns);
    }

    private static javax.swing.table.DefaultTableModel getAgentBookingsModel() {
        String[] columns = {"Booking Reference", "Customer", "Flight", "Status", "Payment Status"};
        List<Booking> bookings = new FileManager().loadBookings();

        Object[][] data = new Object[bookings.size()][5];
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            data[i] = new Object[]{
                b.getBookingReference(),
                b.getCustomer(),
                b.getFlight(),
                b.getStatus(),
                b.getPaymentStatus()
            };
        }

        return new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static void showFlightDialog(String title, Flight flight, JTable flightTable) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtFlightNumber = new JTextField(flight != null ? flight.getFlightNumber() : "");
        JTextField txtAirline = new JTextField(flight != null ? flight.getAirline() : "");
        JTextField txtOrigin = new JTextField(flight != null ? flight.getOrigin() : "");
        JTextField txtDestination = new JTextField(flight != null ? flight.getDestination() : "");
        JTextField txtDepartureTime = new JTextField(flight != null ? flight.getDepartureTime() : "");
        JTextField txtArrivalTime = new JTextField(flight != null ? flight.getArrivalTime() : "");
        JTextField txtPrice = new JTextField(flight != null ? flight.getPrices().split(",")[0] : "");

        panel.add(new JLabel("Flight Number:"));
        panel.add(txtFlightNumber);
        panel.add(new JLabel("Airline:"));
        panel.add(txtAirline);
        panel.add(new JLabel("Origin:"));
        panel.add(txtOrigin);
        panel.add(new JLabel("Destination:"));
        panel.add(txtDestination);
        panel.add(new JLabel("Departure Time:"));
        panel.add(txtDepartureTime);
        panel.add(new JLabel("Arrival Time:"));
        panel.add(txtArrivalTime);
        panel.add(new JLabel("Price (Economy):"));
        panel.add(txtPrice);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            String flightNumber = txtFlightNumber.getText().trim();
            String airline = txtAirline.getText().trim();
            String origin = txtOrigin.getText().trim();
            String destination = txtDestination.getText().trim();
            String departureTime = txtDepartureTime.getText().trim();
            String arrivalTime = txtArrivalTime.getText().trim();
            String price = txtPrice.getText().trim();

            // Validate input
            if (flightNumber.isEmpty() || airline.isEmpty() || origin.isEmpty() || destination.isEmpty() ||
                departureTime.isEmpty() || arrivalTime.isEmpty() || price.isEmpty()) {
                showError("All fields are required.");
                return;
            }

            try {
                double economyPrice = Double.parseDouble(price);

                // Update flight in the database
                Agent agent = (Agent) currentUser;
                boolean success = agent.manageFlights(
                    flightNumber, airline, origin, destination, departureTime, arrivalTime, economyPrice, "update"
                );

                if (success) {
                    flightTable.setModel(getFlightTableModel());
                    showSuccess("Flight updated successfully!");
                    dialog.dispose();
                } else {
                    showError("Failed to update flight. Please check the input.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid price format. Please enter a valid number.");
            }
        });

        panel.add(btnSave);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static Flight getFlightById(String flightId) {
        String sql = "SELECT * FROM flights WHERE flight_number = ?";
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
                String prices = rs.getDouble("economy_price") + "";

                return new Flight(flightNumber, airline, origin, destination, departureTime, arrivalTime, null, prices);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching flight: " + e.getMessage());
        }
        return null;
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showChangePasswordDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Change Password", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.decode("#04009A"));

        JTextField txtUsername = new JTextField();
        JPasswordField txtCurrentPassword = new JPasswordField();
        JPasswordField txtNewPassword = new JPasswordField();
        JPasswordField txtConfirmPassword = new JPasswordField();

        // Style the components
        txtUsername.setBackground(Color.decode("#C0FEFC"));
        txtUsername.setForeground(Color.decode("#04009A"));
        txtCurrentPassword.setBackground(Color.decode("#C0FEFC"));
        txtCurrentPassword.setForeground(Color.decode("#04009A"));
        txtNewPassword.setBackground(Color.decode("#C0FEFC"));
        txtNewPassword.setForeground(Color.decode("#04009A"));
        txtConfirmPassword.setBackground(Color.decode("#C0FEFC"));
        txtConfirmPassword.setForeground(Color.decode("#04009A"));

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.decode("#C0FEFC"));
        JLabel lblCurrentPassword = new JLabel("Current Password:");
        lblCurrentPassword.setForeground(Color.decode("#C0FEFC"));
        JLabel lblNewPassword = new JLabel("New Password:");
        lblNewPassword.setForeground(Color.decode("#C0FEFC"));
        JLabel lblConfirmPassword = new JLabel("Confirm New Password:");
        lblConfirmPassword.setForeground(Color.decode("#C0FEFC"));

        // Add password requirements label
        JLabel lblRequirements = new JLabel("<html>Password must contain:<br>- At least 6 letters<br>- At least 2 numbers</html>");
        lblRequirements.setForeground(Color.decode("#C0FEFC"));
        lblRequirements.setFont(new Font(lblRequirements.getFont().getName(), Font.ITALIC, 12));

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblCurrentPassword);
        panel.add(txtCurrentPassword);
        panel.add(lblNewPassword);
        panel.add(txtNewPassword);
        panel.add(lblConfirmPassword);
        panel.add(txtConfirmPassword);
        panel.add(lblRequirements);
        panel.add(new JLabel("")); // Empty label for grid alignment

        JButton btnUpdate = new JButton("Update Password");
        btnUpdate.setBackground(Color.decode("#C0FEFC"));
        btnUpdate.setForeground(Color.decode("#04009A"));
        btnUpdate.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String currentPassword = new String(txtCurrentPassword.getPassword());
            String newPassword = new String(txtNewPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            // Validate input
            if (username.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showError("All fields are required.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                showError("New passwords do not match.");
                return;
            }

            // Validate password requirements
            int letterCount = 0;
            int numberCount = 0;
            for (char c : newPassword.toCharArray()) {
                if (Character.isLetter(c)) {
                    letterCount++;
                } else if (Character.isDigit(c)) {
                    numberCount++;
                }
            }

            if (letterCount < 6) {
                showError("Password must contain at least 6 letters.");
                return;
            }
            if (numberCount < 2) {
                showError("Password must contain at least 2 numbers.");
                return;
            }

            // Verify current password
            User user = User.login(username, currentPassword);
            if (user == null) {
                showError("Current password is incorrect.");
                return;
            }

            // Update password in database
            String sql = "UPDATE users SET password = ? WHERE username = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newPassword);
                stmt.setString(2, username);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showSuccess("Password updated successfully!");
                    dialog.dispose();
                } else {
                    showError("Failed to update password.");
                }
            } catch (SQLException ex) {
                showError("Error updating password: " + ex.getMessage());
            }
        });

        panel.add(btnUpdate);
        dialog.add(panel);
        dialog.setVisible(true);
    }
}