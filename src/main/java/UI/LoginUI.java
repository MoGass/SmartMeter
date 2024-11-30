package UI;
import Database.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI {

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginUI() {
        // Setup Frame
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(3, 2, 10, 10));

        // UI elements
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");

        // Add elements to Frame
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel());  // Placeholder
        frame.add(loginButton);

        // Login action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Database.createUserTable();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                handleLogin(username, password); // Call function
            }
        });

        frame.setVisible(true);
    }

    private void handleLogin(String username, String password) {
        // Dummy logic
        if(!(User.autheticateUser(username, password))) {
            JOptionPane.showMessageDialog(frame, "Ungültiger Benutzername oder Passwort.");
            return;
        }
        if(User.checkIsAdmin(username)){
            NetzbetreiberUI netzbetreiberUI = new NetzbetreiberUI();
            frame.dispose();
        } else {
            VerbrauchUI verbrauchUI = new VerbrauchUI(username);
            frame.dispose();
        }
    }

    public static void main(String[] args) {
        Database.getReady();
        new LoginUI();  // Start the UI
    }
}