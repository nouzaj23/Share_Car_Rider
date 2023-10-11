package cz.muni.fi.pv168.project.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TemplatesPanel extends JPanel {

    private final JTable table;
    public TemplatesPanel() {
        setLayout(new BorderLayout());
        table = setUpTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private JTable setUpTable() {
        var table = new JTable();
        table.setAutoCreateRowSorter(true);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Currency");
        model.addColumn("Category");
        model.addColumn("From");
        model.addColumn("To");
        model.addColumn("Distance");
        model.addRow(new Object[]{"Name1", "Currency1", "Category1", "From1", "To1", "Distance1"});

        table.setModel(model);
        return table;
    }
}
