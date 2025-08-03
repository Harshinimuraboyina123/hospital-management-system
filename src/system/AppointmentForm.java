package system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentForm extends JFrame {
    private JComboBox<String> patientComboBox, doctorComboBox;
    private JTextField dateField, timeField;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentForm() {
        setTitle("Appointment Management - Hospital System");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel for booking
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        inputPanel.add(new JLabel("Select Patient:"));
        patientComboBox = new JComboBox<>(new String[] { "Alice", "Bob", "Charlie" });
        inputPanel.add(patientComboBox);

        inputPanel.add(new JLabel("Select Doctor:"));
        doctorComboBox = new JComboBox<>(new String[] { "Dr. Smith", "Dr. Jones" });
        inputPanel.add(doctorComboBox);

        inputPanel.add(new JLabel("Appointment Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Appointment Time (HH:MM:SS):"));
        timeField = new JTextField();
        inputPanel.add(timeField);

        JButton bookButton = new JButton("Book Appointment");
        inputPanel.add(bookButton);

        add(inputPanel, BorderLayout.NORTH);

        // Table for viewing appointments
        String[] columns = { "Patient", "Doctor", "Date", "Time" };
        tableModel = new DefaultTableModel(columns, 0);
        appointmentTable = new JTable(tableModel);
        add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        // 🔹 Book Appointment Action with MySQL insert
        bookButton.addActionListener(e -> {
            String patient = (String) patientComboBox.getSelectedItem();
            String doctor = (String) doctorComboBox.getSelectedItem();
            String date = dateField.getText().trim(); // format: YYYY-MM-DD
            String time = timeField.getText().trim(); // format: HH:MM:SS

            if (!date.isEmpty() && !time.isEmpty()) {
                // Add to table
                tableModel.addRow(new Object[] { patient, doctor, date, time });

                // Save to MySQL
                MySQLDatabaseHelper.insertAppointment(patient, doctor, date, time);

                JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter date and time.");
            }
        });

        // 🔹 Load existing appointments from MySQL
        loadAppointmentsFromDatabase();
    }

    private void loadAppointmentsFromDatabase() {
        ResultSet rs = MySQLDatabaseHelper.getAllAppointments();
        try {
            while (rs.next()) {
                String patient = rs.getString("patient_name");
                String doctor = rs.getString("doctor_name");
                String date = rs.getDate("appointment_date").toString();
                String time = rs.getTime("appointment_time").toString();
                tableModel.addRow(new Object[] { patient, doctor, date, time });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentForm().setVisible(true));
    }
}
