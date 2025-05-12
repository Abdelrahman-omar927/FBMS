package Models;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DBConnection;

public class Payment {
    private String paymentId;
    private String bookingReference;
    private String amount;
    private String method;
    private String status;
    private String transactionDate;

    public Payment(String paymentId, String bookingReference, String amount, String method, String status, String transactionDate) {
        this.paymentId = paymentId;
        this.bookingReference = bookingReference;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.transactionDate = transactionDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void processPayment() {
        try (Connection connection = DBConnection.getConnection()) {
            // First get the booking_id from the booking reference
            String getBookingIdSql = "SELECT id FROM bookings WHERE reference = ?";
            PreparedStatement getBookingStmt = connection.prepareStatement(getBookingIdSql);
            getBookingStmt.setString(1, this.bookingReference);
            ResultSet rs = getBookingStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Booking reference not found: " + this.bookingReference);
                return;
            }
            
            int bookingId = rs.getInt("id");
            
            // Generate payment ID if not provided
            if (this.paymentId == null || this.paymentId.isEmpty()) {
                this.paymentId = "PAY" + System.currentTimeMillis();
            }
            
            // Insert payment record
            String sql = "INSERT INTO payments (id, booking_id, amount, method, status, transaction_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.paymentId);
            statement.setInt(2, bookingId);
            statement.setString(3, this.amount);
            statement.setString(4, this.method);
            statement.setString(5, this.status);
            statement.setString(6, this.transactionDate);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // Update booking payment status
                String updateBookingSql = "UPDATE bookings SET payment_status = ? WHERE id = ?";
                PreparedStatement updateBookingStmt = connection.prepareStatement(updateBookingSql);
                updateBookingStmt.setString(1, "Completed");
                updateBookingStmt.setInt(2, bookingId);
                updateBookingStmt.executeUpdate();
                
                System.out.println("Payment processed successfully.");
            } else {
                System.out.println("Failed to process payment.");
            }
        } catch (SQLException e) {
            System.out.println("Error while processing payment: " + e.getMessage());
        }
    }

    public void validatePaymentDetails() {
        if (bookingReference == null || bookingReference.isEmpty()) {
            throw new IllegalArgumentException("Booking Reference cannot be null or empty.");
        }
        if (amount == null || amount.isEmpty()) {
            throw new IllegalArgumentException("Amount cannot be null or empty.");
        }
        try {
            double parsedAmount = Double.parseDouble(amount);
            if (parsedAmount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a valid number.");
        }
        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be null or empty.");
        }
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Payment status cannot be null or empty.");
        }
        if (transactionDate == null || transactionDate.isEmpty()) {
            throw new IllegalArgumentException("Transaction date cannot be null or empty.");
        }
    }

    public void updateStatus() {
        try (Connection connection = DBConnection.getConnection()) {
            
            String sql = "UPDATE payments SET status = ? WHERE paymentId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.status); 
            statement.setString(2, this.paymentId); 

            
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Payment status updated successfully.");
            } else {
                System.out.println("Failed to update payment status. Payment ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error while updating payment status: " + e.getMessage());
        }
    }
}
