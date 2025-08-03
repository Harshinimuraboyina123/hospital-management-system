package system;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DoctorDashboard extends JFrame {
    JTable patientTable;
    JTextField searchField, prescriptionField;
    int selectedPatientId = -1;
    DefaultTableModel model;

    Connection conn;

    public DoctorDashboard() {
        setTitle("Doctor Dashboard - Hospital Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel with search
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search Patient (Name or ID):"));
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchField);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(
                new String[] { "Patient ID", "Name", "Age", "Gender", "Diagnosis", "Prescription" }, 0);
        patientTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Prescription:"));
        prescriptionField = new JTextField(20);
        JButton updateButton = new JButton("Update Prescription");
        bottomPanel.add(prescriptionField);
        bottomPanel.add(updateButton);
        add(bottomPanel, BorderLayout.SOUTH);

        connectDatabase();
        loadPatients();

        // Search Action
        searchButton.addActionListener(e -> searchPatients());

        // Update Action
        updateButton.addActionListener(e -> updatePrescription());

        // Row Selection
        patientTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedPatientId = (int) model.getValueAt(selectedRow, 0);
                    prescriptionField.setText((String) model.getValueAt(selectedRow, 5));
                }
            }
        });

        setVisible(true);
    }

    void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Hpm@1234AB!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }
    }

    void loadPatients() {
        model.setRowCount(0);
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patients");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("diagnosis"),
                        rs.getString("prescription")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void searchPatients() {
        String input = searchField.getText();
        model.setRowCount(0);
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT * FROM patients WHERE name LIKE ? OR patient_id = ?");
            stmt.setString(1, "%" + input + "%");
            try {
                stmt.setInt(2, Integer.parseInt(input));
            } catch (NumberFormatException e) {
                stmt.setInt(2, -1);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("diagnosis"),
                        rs.getString("prescription")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updatePrescription() {
        if (selectedPatientId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.");
            return;
        }
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE patients SET prescription = ? WHERE patient_id = ?");
            stmt.setString(1, prescriptionField.getText());
            stmt.setInt(2, selectedPatientId);
            stmt.executeUpdate();
            loadPatients();
            JOptionPane.showMessageDialog(this, "Prescription updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DoctorDashboard();
    }
}
