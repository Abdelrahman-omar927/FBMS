package Models;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DBConnection;

public class Passenger {
    private String passengerId, name, passportNumber, dateOfBirth, specialRequests ;
    public Passenger(String passengerId, String name, String passportNumber, String dateOfBirth, String specialRequests) {
        this.passengerId = passengerId;
        this.name = name;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
        this.specialRequests = specialRequests;
    }
    public String getPassengerId() {
        return passengerId;
    }
    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassportNumber() {
        return passportNumber;
    }
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getSpecialRequests() {
        return specialRequests;
    }
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    public void updateInfo(){
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE passengers SET name = ?, passportNumber = ?, dateOfBirth = ?, specialRequests = ? WHERE passengerId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.name);
            statement.setString(2, this.passportNumber);
            statement.setString(3, this.dateOfBirth);
            statement.setString(4, this.specialRequests);
            statement.setString(5, this.passengerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getPassengerDetails( ){
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM passengers WHERE passengerId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.passengerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                this.name = resultSet.getString("name");
                this.passportNumber = resultSet.getString("passportNumber");
                this.dateOfBirth = resultSet.getString("dateOfBirth");
                this.specialRequests = resultSet.getString("specialRequests");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
