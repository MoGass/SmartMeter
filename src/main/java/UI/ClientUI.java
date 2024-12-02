package UI;
import Network.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI {

    private JFrame frame;
    private JButton connectButton;
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String TRUSTSTORE_PATH = System.getProperty("user.dir") + "/client.truststore";
    private static final String TRUSTSTORE_PASSWORD = "Admin123!";
    private static final String KEYSTORE_PATH = System.getProperty("user.dir") + "/client.keystore";
    private static final String KEYSTORE_PASSWORD = "Admin123!";

    public ClientUI() {

        System.out.println(TRUSTSTORE_PATH);
        frame = new JFrame("Client Control");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);

        // Verbindungsbutton wird hinzugefügt
        connectButton = new JButton("Mit Server verbinden");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = new Client(HOST, PORT, TRUSTSTORE_PATH, TRUSTSTORE_PASSWORD, KEYSTORE_PATH, KEYSTORE_PASSWORD);
                client.connect();
                frame.dispose();
            }
        });

        frame.add(connectButton);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ClientUI();
    }
}
