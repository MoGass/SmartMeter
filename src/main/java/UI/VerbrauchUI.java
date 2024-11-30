package UI;
import Database.Data;
import Database.DataType;
import Database.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class VerbrauchUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JLabel userInfoLabel;
    private JLabel gesamtVerbrauchLabel;

    private String username;

    public VerbrauchUI(String username) {
        this.username = username;
        // Fenster erstellen
        frame = new JFrame("Verbrauchsdaten für Benutzer: " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        // Spaltennamen für die Tabelle
        String[] spaltenNamen = {"Verbrauchs-ID", "Datum", "Verbrauch (kWh)"};

        // Model für die Tabelle erstellen
        model = new DefaultTableModel(spaltenNamen, 0);

        // Tabelle erstellen und Model hinzufügen
        table = new JTable(model);

        // Sortierbare Tabelle
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // ScrollPane für die Tabelle hinzufügen
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel für Benutzerinformationen und Gesamtverbrauch
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        userInfoLabel = new JLabel("Benutzer-ID: " + User.getIdbyUser(this.username) + " | Benutzername: " + username);


        infoPanel.add(userInfoLabel);


        // Aktualisieren-Button
        JButton aktualisierenButton = new JButton("Aktualisieren");
        aktualisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aktualisiereTabelle();
            }
        });
        infoPanel.add(aktualisierenButton);

        // Panel für Tabelle und Informationen zusammenfügen
        frame.setLayout(new BorderLayout());
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Fenster sichtbar machen
        aktualisiereTabelle();
        frame.setVisible(true);

    }

    // Methode zum Aktualisieren der Tabelle
    public void aktualisiereTabelle() {
        model.setRowCount(0); // Lösche alle aktuellen Zeilen
        // Füge neue Daten hinzu
        List<DataType> list = Data.getVerbauchsData(this.username);
        for (DataType verbrauch : list) {
            // Füge jede Zeile zur Tabelle hinzu
            model.addRow(new Object[]{verbrauch.getId(), verbrauch.getZeitstempel(), verbrauch.getVerbrauch()});
        }

    }


}


