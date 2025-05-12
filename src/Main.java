import Models.User;
import db.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connection to SQLite has been established.");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        User user =new User("1","username","password","name","email","contact");
        user.register("abdelrahman", "abdelrahman", "abdelrahman", "abdelrahman", "abdelrahman", "Customer");
        
    }

}



