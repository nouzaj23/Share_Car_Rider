package cz.muni.fi.pv168.project.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CategoriesPanel extends JPanel {

    private final JTable table;
    public CategoriesPanel() {
        setLayout(new BorderLayout());
        table = setUpTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
    }


    private JTable setUpTable() {
        var table = new JTable();
        table.setAutoCreateRowSorter(true);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Number of rides");
        model.addColumn("Distance");
        model.addRow(new Object[]{"Name1", "123", "456"});

        table.setModel(model);
        return table;
    }
}
