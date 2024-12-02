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

        frame = new JFrame("Netzbetreiber - Messtellenverwaltung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Dropdown Menu der Messstellen
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


        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Aktualisierung der Tabelle mit Hilfe des Dropdowns
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
        List<DataType> daten = Data.getAllData();
        return daten.stream()
                .map(data -> "Messtelle " + data.getId())
                .distinct()
                .toArray(String[]::new);
    }

    // Aktualisiert die Tabelle basierend auf der ausgewählten Messtelle
    private void updateTableData() {

        tableModel.setRowCount(0);

        String selectedMesstelle = (String) messtellenDropdown.getSelectedItem();
        if (selectedMesstelle != null) {
            int messtellenId = Integer.parseInt(selectedMesstelle.replace("Messtelle ", ""));
            List<DataType> daten = Data.getVerbauchsData(messtellenId);
            for (DataType data : daten) {
                tableModel.addRow(new Object[]{data.getZeitstempel(), data.getVerbrauch()});
            }
        }
    }
//Main Methode zum Testen
    public static void main(String[] args) {
        new NetzbetreiberUI();
    }
}