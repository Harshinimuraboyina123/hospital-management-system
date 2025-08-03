package system;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Launch your initial form here. Example:
            new LoginForm(); // or new AdminDashboard(); etc.
        });
    }
}
