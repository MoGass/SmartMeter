package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Data {
    /*
    Sicherheitsaspekte:
     Verwende eines PreparedStatemt gegen SQLInjection
     Hier wird ein Datensatz eingepflegt, falls dieser noch nicht exisitiert.
     Jeder Datensatz wird mit der ID eines Nutzers verbunden.
     */
    public static void insertMesstelleDataIfNotExists(int messtellenId, int userId, String datum, int verbrauch) {
        String checkDataSQL = "SELECT COUNT(*) FROM data WHERE messtellen_id = ? AND user_id = ? AND datum = ? AND verbrauch = ?";
        String insertDataSQL = "INSERT INTO data (messtellen_id, user_id, datum, verbrauch) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkDataSQL);
             PreparedStatement insertStatement = connection.prepareStatement(insertDataSQL)) {

            checkStatement.setInt(1, messtellenId);
            checkStatement.setInt(2, userId);
            checkStatement.setString(3, datum);
            checkStatement.setInt(4, verbrauch);
            ResultSet resultSet = checkStatement.executeQuery();

            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                insertStatement.setInt(1, messtellenId);
                insertStatement.setInt(2, userId);
                insertStatement.setString(3, datum);
                insertStatement.setInt(4, verbrauch);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
     Mit dem Usernamen werden seine persönlichen Verbrauchsdaten ermittelt
     Sicherheitsaspekte:
     Verwende eines PreparedStatemt gegen SQLInjection
     Es werden nur die Daten übermittelt, die mit dem Usernamen verknüpft sind. Somit ist der Datenschutz hergestellt.
     */
    public static List<DataType> getVerbauchsData(String username) {
        List<DataType> list = new ArrayList<>();
        int userId = User.getIdbyUser(username);
        String getDataSQL = "SELECT messtellen_id, datum, verbrauch FROM data WHERE user_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDataSQL)) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int messtellenId = resultSet.getInt("messtellen_id");
                String datum = resultSet.getString("datum");
                int verbrauch = resultSet.getInt("verbrauch");

                list.add(new DataType(messtellenId, datum, verbrauch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Sammeln der Verbrauchsdaten.");
        }

        return list;
    }

    /*
    Mit dem MesstellenID werden alle Verbrauchsdaten der Messtelle ermittelt
     Sicherheitsaspekte:
     Verwende eines PreparedStatemt gegen SQLInjection
     Es werden nur die Daten übermittelt, die mit dem MesstellenID verknüpft sind. Somit ist der Datenschutz hergestellt.
     */
    public static List<DataType> getVerbauchsData(int id) {
        List<DataType> list = new ArrayList<>();
        int messtellen_id = id;

        String getDataSQL = "SELECT messtellen_id, datum, verbrauch FROM data WHERE messtellen_id = ?";

        try (Connection connection =Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDataSQL)) {
            preparedStatement.setInt(1, messtellen_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int messtellenId = resultSet.getInt("messtellen_id");
                String datum = resultSet.getString("datum");
                int verbrauch = resultSet.getInt("verbrauch");

                list.add(new DataType(messtellenId, datum, verbrauch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Sammeln der Verbrauchsdaten.");
        }

        return list;
    }

    /*
    Admin Methode, Hiermit können alle VerbrauchsDaten eingesammelt werden.
    Dient zur Anzeige für den Messtellenbetreiber.
     */
    public static List<DataType> getAllData() {
        List<DataType> list = new ArrayList<>();
        String getDataSQL = "SELECT messtellen_id, datum, verbrauch FROM data";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDataSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int messtellenId = resultSet.getInt("messtellen_id");
                String datum = resultSet.getString("datum");
                int verbrauch = resultSet.getInt("verbrauch");

                list.add(new DataType(messtellenId, datum, verbrauch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Sammeln der Verbrauchsdaten.");
        }
        return list;
    }
}