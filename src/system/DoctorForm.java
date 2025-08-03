package system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import system.DBConnection;

public class DoctorForm extends JFrame {
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, updateButton;
    private JTextField prescriptionField;

    public DoctorForm() {
        setTitle("Doctor Dashboard - Hospital Management System");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel with search
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        topPanel.add(new JLabel("Search Patient (Name or ID):"));
        searchField = new JTextField(20);
        topPanel.add(searchField);

        searchButton = new JButton("Search");
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        // Table for patients
        String[] columnNames = { "Patient ID", "Name", "Age", "Gender", "Diagnosis", "Prescription" };
        tableModel = new DefaultTableModel(columnNames, 0);
        patientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with prescription and update
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        bottomPanel.add(new JLabel("Prescription:"));
        prescriptionField = new JTextField(20);
        bottomPanel.add(prescriptionField);

        updateButton = new JButton("Update Prescription");
        bottomPanel.add(updateButton);

        add(bottomPanel, BorderLayout.SOUTH);

        loadAllPatients(); // Initially load all patients

        // Button actions
        searchButton.addActionListener(e -> searchPatients());
        updateButton.addActionListener(e -> updatePrescription());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadAllPatients() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM patients";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear table
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("diagnosis"),
                        rs.getString("prescription")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading patients: " + ex.getMessage());
        }
    }

    private void searchPatients() {
        String keyword = searchField.getText().trim();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM patients WHERE name LIKE ? OR patient_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, keyword.matches("\\d+") ? keyword : "-1");

            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("diagnosis"),
                        rs.getString("prescription")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching: " + ex.getMessage());
        }
    }

    private void updatePrescription() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient from the table.");
            return;
        }

        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        String newPrescription = prescriptionField.getText().trim();

        try (Connection conn = DBConnection.getConnection()) {
            String updateQuery = "UPDATE patients SET prescription = ? WHERE patient_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, newPrescription);
            stmt.setInt(2, patientId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                tableModel.setValueAt(newPrescription, selectedRow, 5); // update table UI
                JOptionPane.showMessageDialog(this, "Prescription updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating prescription: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new DoctorForm();
    }
}
