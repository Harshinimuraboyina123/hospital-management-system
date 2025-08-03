package system;

import java.sql.*;

public class MySQLDatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital"; // Change database name if different
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASS = "Hpm@1234AB!"; // Change to your MySQL password

    public static Connection connect() {
        try {
            // ✅ Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Connection to database failed.");
            e.printStackTrace();
            return null;
        }
    }

    public static void insertAppointment(String patient, String doctor, String date, String time) {
        String sql = "INSERT INTO Appointments (patient_name, doctor_name, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient);
            pstmt.setString(2, doctor);
            pstmt.setDate(3, Date.valueOf(date)); // format: YYYY-MM-DD
            pstmt.setTime(4, Time.valueOf(time + ":00")); // Add seconds
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getAllAppointments() {
        String sql = "SELECT * FROM Appointments";
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
