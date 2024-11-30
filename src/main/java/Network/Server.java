package Network;
import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
public class Server {


    private int port;
    private String keyStorePath;
    private String keyStorePassword;
    private String trustStorePath;
    private String trustStorePassword;
    private boolean running;

    public Server(int port) {
        this.port = port;
        this.keyStorePath = System.getProperty("user.dir") + "/server.keystore";
        this.keyStorePassword = "Admin123!";
        this.trustStorePath = System.getProperty("user.dir") + "/server.truststore";
        this.trustStorePassword = "Admin123!";
    }

    public void startServer() throws Exception {
        Database.Database.getReady();

        // KeyStore für den Server laden
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

        // TrustStore für den Server laden (für die Überprüfung des Clients)
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trustStorePath), trustStorePassword.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);

        // SSLContext konfigurieren
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        // SSLServerSocket erstellen
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);

        // Erzwinge die gegenseitige Authentifizierung
        sslServerSocket.setNeedClientAuth(true);

        System.out.println("Server gestartet und wartet auf Verbindungen auf Port " + port + "...");
        running = true;
        while (running) {
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
            System.out.println("Client verbunden");

            // Lese und schreibe Daten vom Client
            BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

            String clientMessage = reader.readLine();
            System.out.println("Client sagt: " + clientMessage);

            writer.write("Hallo vom Server\n");
            writer.flush();

            sslSocket.close();
        }
    }
    public void stopServer() throws Exception {
        running = false;
    }
}
