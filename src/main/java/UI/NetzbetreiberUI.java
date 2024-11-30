package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import Database.*;
import Database.DataType;

public class NetzbetreiberUI {

    private JFrame frame;
    private JComboBox<String> messtellenDropdown;
    private JTable datenTabelle;
    private DefaultTableModel tableModel;

    public NetzbetreiberUI() {
        // Set up Frame
        frame = new JFrame("Netzbetreiber - Messtellenverwaltung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Dropdown für Messtellen
        JPanel controlPanel = new JPanel();
        JLabel messtellenLabel = new JLabel("Messtelle auswählen:");
        messtellenDropdown = new JComboBox<>(getMesstellen());
        controlPanel.add(messtellenLabel);
        controlPanel.add(messtellenDropdown);

        // Tabelle zur Anzeige der Messdaten
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Datum");
        tableModel.addColumn("Verbrauch");

        datenTabelle = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(datenTabelle);

        // Hinzufügen der Komponenten zum Frame
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Aktion für Dropdown-Auswahl
        messtellenDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTableData(); // Funktion zum Aktualisieren der Tabellenanzeige
            }
        });

        frame.setVisible(true);
    }

    // Funktion, um die Messtellen für das Dropdown-Menü zu füllen
    private String[] getMesstellen() {
        List<DataType> daten = Data.getAllData(); // Achte darauf, dass getAllData() wirklich List<DataType> zurückgibt
        return daten.stream()
                .map(data -> "Messtelle " + data.getId()) // Wandelt jede Messtellen-ID in den gewünschten String um
                .distinct() // Nur einzigartige Einträge
                .toArray(String[]::new);
    }

    // Aktualisiert die Tabelle basierend auf der ausgewählten Messtelle
    private void updateTableData() {
        // Lösche alle vorhandenen Zeilen in der Tabelle
        tableModel.setRowCount(0);

        // Extrahiere die ausgewählte Messtellen-ID
        String selectedMesstelle = (String) messtellenDropdown.getSelectedItem();
        if (selectedMesstelle != null) {
            int messtellenId = Integer.parseInt(selectedMesstelle.replace("Messtelle ", ""));

            // Holen Sie sich die Daten für die ausgewählte Messtelle
            List<DataType> daten = Data.getVerbauchsData(messtellenId);

            // Füge jede Zeile in die Tabelle ein
            for (DataType data : daten) {
                tableModel.addRow(new Object[]{data.getZeitstempel(), data.getVerbrauch()});
            }
        }
    }

    public static void main(String[] args) {
        new NetzbetreiberUI();
    }
}