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
        frame = new JFrame("Verbrauchsdaten für Benutzer: " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        String[] spaltenNamen = {"Verbrauchs-ID", "Datum", "Verbrauch (kWh)"};
        model = new DefaultTableModel(spaltenNamen, 0);


        table = new JTable(model);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        userInfoLabel = new JLabel("Benutzer-ID: " + User.getIdbyUser(this.username) + " | Benutzername: " + username);


        infoPanel.add(userInfoLabel);


        // Aktualisierung der Messdaten
        JButton aktualisierenButton = new JButton("Aktualisieren");
        aktualisierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aktualisiereTabelle();
            }
        });
        infoPanel.add(aktualisierenButton);


        frame.setLayout(new BorderLayout());
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        aktualisiereTabelle();
        frame.setVisible(true);

    }

    // Methode zum Aktualisieren der Tabelle
    public void aktualisiereTabelle() {
        model.setRowCount(0);
         List<DataType> list = Data.getVerbauchsData(this.username);
        for (DataType verbrauch : list) {
            model.addRow(new Object[]{verbrauch.getId(), verbrauch.getZeitstempel(), verbrauch.getVerbrauch()});
        }

    }


}


