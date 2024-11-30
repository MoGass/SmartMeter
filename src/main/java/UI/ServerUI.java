package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerUI {

    private JFrame frame;
    private JButton serverToggleButton;
    private static final int PORT = 8080; // Fester Port für den Server
    private boolean isServerRunning = false;
    private Network.Server server;

    public ServerUI() {
        // Setup Frame
        frame = new JFrame("Server Control");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new BorderLayout());

        // Toggle Button für Start/Stop
        serverToggleButton = new JButton("Server Starten");
        serverToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    toggleServer();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Button zum Frame hinzufügen
        frame.add(serverToggleButton, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void toggleServer() throws Exception {
        if (isServerRunning) {
            stopServer();
        } else {
            startServer();
        }
    }

    private void startServer() throws Exception {
        if (!isServerRunning) {
            System.out.println("Server gestartet auf Port " + PORT);
            isServerRunning = true;

            server = new Network.Server(PORT);
            server.startServer();

            updateButtonLabel();
        }
    }

    private void stopServer() throws Exception {
        if (isServerRunning) {
            System.out.println("Server gestoppt.");
            isServerRunning = false;

            if (server != null) {
                server.stopServer();
            }

            updateButtonLabel();
        }
    }

    private void updateButtonLabel() {
        serverToggleButton.setText(isServerRunning ? "Server Stoppen" : "Server Starten");
    }

    public static void main(String[] args) {
        new ServerUI(); // Start der ServerUI
    }
}