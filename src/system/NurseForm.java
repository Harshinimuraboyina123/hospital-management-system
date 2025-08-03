package system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NurseForm extends JFrame {

    public NurseForm() {
        setTitle("Nurse Dashboard");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton viewPatientsButton = new JButton("View Patients");
        JButton updateVitalsButton = new JButton("Update Vitals");
        JButton logoutButton = new JButton("Logout");

        // Add ActionListeners
        viewPatientsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Displaying patient list...");
                // In real project: Open patient view window
            }
        });

        updateVitalsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Update vitals screen...");
                // In real project: Open update vitals form
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close NurseForm window
                JOptionPane.showMessageDialog(null, "Logged out successfully.");
                // In real project: Redirect to LoginForm
            }
        });

        panel.add(viewPatientsButton);
        panel.add(updateVitalsButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }
}
