package system;

import javax.swing.*;
import java.awt.event.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        setTitle("Hospital Management System - Dashboard");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton patientButton = new JButton("Patient Form");
        patientButton.setBounds(100, 50, 200, 30);
        add(patientButton);

        JButton doctorButton = new JButton("Doctor Form");
        doctorButton.setBounds(100, 100, 200, 30);
        add(doctorButton);

        JButton nurseButton = new JButton("Nurse Form");
        nurseButton.setBounds(100, 150, 200, 30);
        add(nurseButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(100, 200, 200, 30);
        add(exitButton);

        // Action Listeners
        patientButton.addActionListener(e -> new PatientForm());
        doctorButton.addActionListener(e -> new DoctorForm());
        nurseButton.addActionListener(e -> new NurseForm());
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboard());
    }
}
