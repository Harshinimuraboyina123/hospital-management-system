package system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import system.MySQLDatabaseHelper;

public class PatientForm extends JFrame {

    public PatientForm() {
        setTitle("Patient Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();

        JLabel genderLabel = new JLabel("Gender:");
        String[] genders = { "Male", "Female", "Other" };
        JComboBox<String> genderCombo = new JComboBox<>(genders);

        JButton submitButton = new JButton("Submit");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(genderLabel);
        panel.add(genderCombo);
        panel.add(new JLabel()); // Empty cell
        panel.add(submitButton);

        add(panel);
        setVisible(true);

        // Submit button logic
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String ageText = ageField.getText().trim();
                String gender = (String) genderCombo.getSelectedItem();

                if (name.isEmpty() || ageText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields.");
                    return;
                }

                try {
                    int age = Integer.parseInt(ageText);

                    // Insert into database
                    try (Connection conn = MySQLDatabaseHelper.connect()) {
                        String sql = "INSERT INTO Patients (name, age, gender) VALUES (?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, name);
                        stmt.setInt(2, age);
                        stmt.setString(3, gender);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Patient added successfully!");
                        dispose(); // close the form
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Age must be a number.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding patient: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PatientForm());
    }
}
