package Database;

import java.sql.*;


public class User {

    /*
    Es wird ein neuer Nutzer hinzugefügt.
    Dabei wird das Passwort überprüft, ob es alle Anforderungen erfüllt.
    Es wird auch überprüft, ob der Username einzigartig ist. Damit die Identität eines Nutzers einzigartig ist und nicht
    missbraucht werden kann.

     */
    public static void addUser(String lastName, String firstName, String email,String username, String password, Boolean isAdmin) {
        int ID = User.getIdbyUser(username);
        if(ID != -1){
            System.out.println("User existiert bereits mit folgender ID: " + ID);
            return;
        }

        if (!SecurityChecks.checkPassword(password)) {

            System.out.println("Password erfüllt die Standards nicht");
            return;
        }
        String insertUserSQL = "INSERT INTO users (name, vorname, email, username, password, admin) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {

            // Setze die Parameter für das PreparedStatement
            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, password);
            preparedStatement.setBoolean(6, isAdmin);

            // Führe die SQL-Anweisung aus
            preparedStatement.executeUpdate();
            System.out.println("Benutzer '" + username + "' wurde erfolgreich hinzugefügt.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Hinzufügen des Benutzers '" + username + "'.");
        }

    }
    /*
    Man erhält die ID für den übermittelten Username
     */
public static int getIdbyUser(String username) {
        String selectUserSQL = "SELECT id FROM users WHERE username = ?";
    try (Connection connection = Database.getConnection()) {
        PreparedStatement preparedStatement = connection.prepareStatement(selectUserSQL);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            return resultSet.getInt("id");
        }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return -1;
}
/*
    Überprüfung des Loginzugangs. Damit wird das Zugangsmanagementsystem sichergestellt.
 */
public static boolean autheticateUser(String username, String password) {
        String selectUserSQL = "SELECT password FROM users WHERE username = ?";
    try (Connection connection = Database.getConnection()) {
        PreparedStatement preparedStatement = connection.prepareStatement(selectUserSQL);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            if (resultSet.getString("password").equals(password)){
                return true;
            }
            return false;
        }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    return false;
}
/*
Hier wird gecheckt, ob der Nutzer ein Admin ist.
Damit wird das Least-Privelege-Prinzip erhalten. Ist er kein Admin, sieht er nur seine Daten
 */
    public static boolean checkIsAdmin(String username) {
        String query = "SELECT admin FROM users WHERE id = ?";

        try (Connection connection = Database.getConnection()) {
            // Hole die Benutzer-ID durch die Funktion getIdbyUser
            int userId = getIdbyUser(username);

            if (userId == -1) {
                return false; // Username exisitiert nicht, also auch kein Admin
            }

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, userId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("admin"); // Admin-Status zurückgeben
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}