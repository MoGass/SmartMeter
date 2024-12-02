package Network;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import UI.*;

public class Client {

    private String host;
    private int port;
    private String trustStorePath;
    private String trustStorePassword;
    private String keyStorePath;
    private String keyStorePassword;

    public Client(String host, int port, String trustStorePath, String trustStorePassword,String keyStorePath,
                  String keyStorePassword) {
        this.host = host;
        this.port = port;
        this.trustStorePath = trustStorePath;
        this.trustStorePassword = trustStorePassword;
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
    }
    /*
    Mithilfe eines Zertifikates wird eine sichere Verbindung zum Server aufgebaut.
    Ergänzen: Unterstützung für gegenseitige Authentifizierung.
    */
    public void connect() {
        try {
            // TrustStore setzen (für die Authentifizierung des Servers)
            System.setProperty("javax.net.ssl.trustStore", trustStorePath);
            System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);

            // KeyStore setzen (für die Authentifizierung des Clients)
            System.setProperty("javax.net.ssl.keyStore", keyStorePath);
            System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);

            // SSL-Socket-Factory initialisieren
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            // Verbindung zum Server herstellen
            try (SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port)) {
                System.out.println("Verbindung zum Server hergestellt.");

                // Handshake initiieren
                sslSocket.startHandshake();
                System.out.println("Handshake abgeschlossen.");

                // Dummy-Kommunikation (nur zur Überprüfung der Verbindung)
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(sslSocket.getOutputStream()), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

                writer.println("Hallo Server!");
                System.out.println("Nachricht an den Server gesendet: Hallo Server!");

                String serverResponse = reader.readLine();
                System.out.println("Antwort vom Server: " + serverResponse);

                // Wenn die Verbindung erfolgreich authentifiziert ist
                if ("Hallo vom Server".equals(serverResponse)) {
                    System.out.println("Authentifizierung erfolgreich. Login UI wird geöffnet.");
                    //LoginUI wird gestartet, um den nächsten Schritt der Authentifizierung zu gehen
                    //Reele Implementierung würde die Kommunikation hier weiter über Client-Server laufen,
                    //In diesem Projekt wird dies vereinfacht und der Client greift direkt auf die Datenbank zu.
                    new LoginUI();
                } else {
                    System.err.println("Unerwartete Antwort vom Server.");
                }
            }
        } catch (Exception e) {
            System.err.println("Fehler bei der Verbindung zum Server:");
            e.printStackTrace();
        }
    }
}