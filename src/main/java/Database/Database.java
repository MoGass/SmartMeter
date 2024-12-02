package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private static final String USER = "sa"; // Standardbenutzername für H2
    private static final String PASSWORD = "Admin123!"; //Password des USER
    private static final String ENCRYPTION_KEY = "ZxYvWpQsRnMtLuKjHgFdSaPqOlNk98&*"; // AES-Verschlüsselungsschlüssel
    private static final String COMBINED_PASSWORD = PASSWORD + " " + ENCRYPTION_KEY; // Kombiniertes Passwort
    private static String JDBC_URL; // Dynamisch festgelegt

    static {
        initializeDatabasePath();
    }

    /* Setzt den verschlüsselten Datenbankpfad */
    private static void initializeDatabasePath() {
        String projectPath = new java.io.File("").getAbsolutePath();
        String databasePath = projectPath + "/userdb";
        JDBC_URL = "jdbc:h2:" + databasePath + ";CIPHER=AES";
    }

    /* Verbindung zur verschlüsselten Datenbank herstellen, wenn diese exisitiert.
       Falls nicht, wird eine neue Datenbank erzeugt
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, COMBINED_PASSWORD);
    }

    /* Erstellt die Tabelle USERS, falls sie nicht existiert */
    public static void createUserTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(50), "
                + "vorname VARCHAR(50), "
                + "email VARCHAR(100), "
                + "username VARCHAR(50), "
                + "password VARCHAR(100), "
                + "admin BOOLEAN"
                + ");";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.execute();
            checkAndCreateAdminUser();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Erstellt einen Admin-Dummy-Benutzer, falls keiner existiert */
    private static void checkAndCreateAdminUser() {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE id = 1";
        String insertQuery = "INSERT INTO users (id, name, vorname, email, username, password, admin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
             ResultSet rs = checkStmt.executeQuery()) {

            rs.next();
            if (rs.getInt(1) == 0) {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, 1);
                    insertStmt.setString(2, "Admin");
                    insertStmt.setString(3, "");
                    insertStmt.setString(4, "admin@example.com");
                    insertStmt.setString(5, "admin");
                    insertStmt.setString(6, "Admin123!");
                    insertStmt.setBoolean(7, true);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Erstellt die Tabelle DATA, falls sie nicht existiert */
    public static void createDataTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS data ("
                + "data_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "messtellen_id INT, "
                + "user_id INT, "
                + "datum DATETIME, "
                + "verbrauch INT, "
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                + ");";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Fügt Dummy-Daten ein und bereitet die Datenbank vor */
    public static void getReady() {
        createUserTable();
        createDataTable();
        User.addUser("Mustermann", "Max", "Max@mustermann.de", "Max", "MaxMuster123!", false);
        User.addUser("Musterfrau", "Mara", "Mara@musterfrau.de", "Mara", "MaraMuster123!", false);

        int IDMax = User.getIdbyUser("Max");
        int IDMara = User.getIdbyUser("Mara");

        Data.insertMesstelleDataIfNotExists(1, IDMax, "2024-01-01", 100);
        Data.insertMesstelleDataIfNotExists(1, IDMax, "2024-04-01", 150);
        Data.insertMesstelleDataIfNotExists(1, IDMax, "2024-11-01", 250);

        Data.insertMesstelleDataIfNotExists(2, IDMara, "2024-01-01", 70);
        Data.insertMesstelleDataIfNotExists(2, IDMara, "2024-04-01", 120);
        Data.insertMesstelleDataIfNotExists(2, IDMara, "2024-11-01", 210);
    }
}