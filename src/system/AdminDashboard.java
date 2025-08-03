// AdminDashboard.java
package system;

import javax.swing.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JLabel label = new JLabel("Welcome to Admin Dashboard!", SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
}
