package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarRidesPanel extends JPanel {

    private final JTable table;
    private final JComboBox<Category> categoryFilter = new JComboBox<>();
    private final JComboBox<Currency> currencyFilter = new JComboBox<>(Currency.values());
    private final JTextField passengersFilter = new JTextField(2);
    private final JTextField distanceFilter = new JTextField(5);
    private final JTextField fromFilter = new JTextField(14);
    private final JTextField toFilter = new JTextField(14);
    public CarRidesPanel() {
        setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        // Create and add labels for each filter
        JLabel categoryLabel = new JLabel("Category:");
        JLabel currencyLabel = new JLabel("Currency:");
        JLabel passengersLabel = new JLabel("Passengers:");
        JLabel distanceLabel = new JLabel("Distance:");
        JLabel fromLabel = new JLabel("From:");
        JLabel toLabel = new JLabel("To:");

        // Create and add filter components
        filterPanel.add(categoryLabel);
        filterPanel.add(categoryFilter);
        filterPanel.add(currencyLabel);
        filterPanel.add(currencyFilter);
        filterPanel.add(passengersLabel);
        filterPanel.add(passengersFilter);
        filterPanel.add(distanceLabel);
        filterPanel.add(distanceFilter);
        filterPanel.add(fromLabel);
        filterPanel.add(fromFilter);
        filterPanel.add(toLabel);
        filterPanel.add(toFilter);

        table = setUpTable();

        add(filterPanel, BorderLayout.NORTH);
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
