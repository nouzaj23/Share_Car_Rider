package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.dialog.RideDialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class CarRidesPanel extends AbstractPanel<Ride> {

    private final Action addAction = new AddAction<Ride>(this);
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
        JButton addRow = new JButton("Add Ride");

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

        var toolbar = new JToolBar();
        toolbar.add(addAction);

        add(toolbar, BorderLayout.SOUTH);
        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }


    private JTable setUpTable() {
        return super.setUpTable(new String[]{"Name", "Passengers", "Currency", "Category", "From", "To", "Distance"},
                                new Object[]{"Sluzobka", 2, Currency.CZK, new Category("Sluzobna jazda"),
                                LocalDateTime.now(), LocalDateTime.now(), 130});
    }

    @Override
    public EntityDialog<Ride> getDialog() {
        return new RideDialog(Ride.exampleRide());
    }

    @Override
    public void addRow(Ride entity) {
        getModel().addRow(new Object[] {entity.getName(), entity.getPassengers(), entity.getCurrency(),
                          entity.getCategory(), entity.getFrom(), entity.getTo(), entity.getDistance()});
    }
}
