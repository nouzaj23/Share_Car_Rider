package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.dialog.RideDialog;
import cz.muni.fi.pv168.project.ui.model.CarRidesModel;
import cz.muni.fi.pv168.project.ui.model.CategoryListModel;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.function.Consumer;

public class CarRidesPanel extends AbstractPanel<Ride> {

    private final Consumer<Integer> onSelectionChange;
    private final CarRidesModel carRidesModel;
    private final CategoryListModel categoryListModel;

    public CarRidesPanel(CarRidesModel carRidesModel, CategoryListModel categoryListModel, Consumer<Integer> onSelectionChange) {
        this.carRidesModel = carRidesModel;
        this.categoryListModel = categoryListModel;

        ComboBoxModel<Category> categoryFilter = new ComboBoxModelAdapter<>(categoryListModel);

        this.onSelectionChange = onSelectionChange;
        setLayout(new BorderLayout());

        var toolbar = new JToolBar();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        Action addAction = new AddAction<>(this);
        toolbar.add(new JButton(addAction));

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
        filterPanel.add(new JComboBox<>(categoryFilter));

        filterPanel.add(currencyLabel);
        filterPanel.add(new JComboBox<>(new DefaultComboBoxModel<>(Currency.values())));

        filterPanel.add(passengersLabel);
        filterPanel.add(new JTextField());

        filterPanel.add(distanceLabel);
        filterPanel.add(new JTextField());

        filterPanel.add(fromLabel);
        filterPanel.add(new JTextField());

        filterPanel.add(toLabel);
        filterPanel.add(new JTextField());

        JTable table = setUpTable();

        PanelHelper.createTopBar(this, table, filterPanel);
    }

    private JTable setUpTable() {
        var table = new JTable(carRidesModel);

        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        var currencyComboBox = new JComboBox<>(Currency.values());
        table.setDefaultEditor(Currency.class, new DefaultCellEditor(currencyComboBox));
        var categoryComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        table.setDefaultEditor(Category.class, new DefaultCellEditor(categoryComboBox));

        return table;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();
        if (onSelectionChange != null) {
            onSelectionChange.accept(count);
        }
    }

    @Override
    public EntityDialog<Ride> getDialog() {
        return new RideDialog(Ride.exampleRide(), categoryListModel);
    }

    @Override
    public void addRow(Ride entity) {
        carRidesModel.addRow(entity);
        if (entity.getCategory() != null) {
            categoryListModel.updateRow(entity.getCategory().modifyDistanceFluent(entity.getDistance()));
            entity.getCategory().setRides(entity.getCategory().getRides() + 1);
        }
    }
}
