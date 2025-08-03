package system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddPatientForm extends JFrame {
    JTextField nameField, ageField, genderField, diagnosisField;
    JButton submitButton;

    Connection conn;

    public AddPatientForm() {
        setTitle("Add New Patient");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        nameField = new JTextField();
        ageField = new JTextField();
        genderField = new JTextField();
        diagnosisField = new JTextField();
        submitButton = new JButton("Submit");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Age:"));
        add(ageField);
        add(new JLabel("Gender:"));
        add(genderField);
        add(new JLabel("Diagnosis:"));
        add(diagnosisField);
        add(new JLabel());  // empty cell
        add(submitButton);

        connectDatabase();

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Hpm@1234AB!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }
    }

    void addPatient() {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = genderField.getText();
            String diagnosis = diagnosisField.getText();

            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO patients (name, age, gender, diagnosis, prescription) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, diagnosis);
            stmt.setString(5, ""); // default prescription empty
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Patient added successfully!");
            dispose(); // close window after adding
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddPatientForm();
    }
}
