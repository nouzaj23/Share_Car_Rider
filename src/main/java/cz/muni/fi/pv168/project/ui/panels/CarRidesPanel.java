package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.actions.DeleteAction;
import cz.muni.fi.pv168.project.ui.actions.EditAction;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.dialog.RideDialog;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;
import cz.muni.fi.pv168.project.ui.panels.helper.PopupMenuGenerator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.function.Consumer;

public class CarRidesPanel extends AbstractPanel<Ride> {

    private final Consumer<Integer> onSelectionChange;
    private final CarRidesModel carRidesModel;
    private final CategoryListModel categoryListModel;
    private final JLabel totalDistance;
    private final TemplateModel templates;
    private CategoryModel categoryModel;

    public CarRidesPanel(CarRidesModel carRidesModel, CategoryListModel categoryListModel,
                         Consumer<Integer> onSelectionChange, TemplateModel templates,
                         CategoryModel categoryModel) {

        this.carRidesModel = carRidesModel;
        this.carRidesModel.setLinkedPannel(this);
        this.categoryListModel = categoryListModel;
        this.categoryModel = categoryModel;
        this.templates = templates;

        ComboBoxModel<Category> categoryFilter = new ComboBoxModelAdapter<>(categoryListModel);

        this.onSelectionChange = onSelectionChange;
        setLayout(new BorderLayout());

        var toolbar = new JToolBar();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

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

        this.table = setUpTable();

        totalDistance = new JLabel();
        triggerTotalDistanceUpdate();
        PanelHelper.createTopBar(this, table, filterPanel, totalDistance);
    }

    private JTable setUpTable() {
        var table = new JTable(carRidesModel);

        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        var currencyComboBox = new JComboBox<>(Currency.values());
        table.setDefaultEditor(Currency.class, new DefaultCellEditor(currencyComboBox));
        var categoryComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        table.setDefaultEditor(Category.class, new DefaultCellEditor(categoryComboBox));
        table.setComponentPopupMenu(PopupMenuGenerator.generatePopupMenu(this));
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
        return new RideDialog(Ride.exampleRide(), categoryListModel, templates, categoryModel);
    }

    @Override
    public EntityDialog<Ride> getDialog(Ride entity) {
        return new RideDialog(entity, categoryListModel, templates, categoryModel);
    }

    @Override
    public void addRow(Ride entity) {
        carRidesModel.addRow(entity);
        Category rideCategory = entity.getCategory();
        if (rideCategory != null) {
            categoryListModel.updateRow(rideCategory.modifyDistanceFluent(entity.getDistance()));
            entity.getCategory().setRides(rideCategory.getRides() + 1);
        }
        triggerTotalDistanceUpdate();
    }

    @Override
    public void deleteRow(int rowIndex) {
        Ride ride = carRidesModel.getEntity(rowIndex);
        Category rideCategory = ride.getCategory();
        if (rideCategory != null) {
            categoryListModel.updateRow(rideCategory.modifyDistanceFluent(-ride.getDistance()));
            ride.getCategory().setRides(rideCategory.getRides() - 1);
        }
        carRidesModel.deleteRow(rowIndex);
        triggerTotalDistanceUpdate();
    }

    @Override
    public void editRow(Ride newEntity, Ride oldRide) {
        Category oldCategory = oldRide.getCategory();
        Category newCategory = newEntity.getCategory();
        if (oldCategory == newCategory) {
            if (newCategory != null) {
                categoryListModel.updateRow(newCategory.modifyDistanceFluent(newEntity.getDistance() - oldRide.getDistance()));
            }
        } else {
            if (oldCategory != null) {
                categoryListModel.updateRow(oldCategory.modifyDistanceFluent(-oldRide.getDistance()));
                oldCategory.setRides(oldCategory.getRides() - 1);
            }
            if (newCategory != null) {
                categoryListModel.updateRow(newCategory.modifyDistanceFluent(newEntity.getDistance()));
                newCategory.setRides(newCategory.getRides() + 1);
            }
        }
        carRidesModel.updateRow(newEntity);
        triggerTotalDistanceUpdate();
    }

    public void triggerTotalDistanceUpdate() {
        var result = 0;
        for (int i = 0; i < carRidesModel.getRowCount(); i++) {
            result += carRidesModel.getEntity(i).getDistance();
        }
        totalDistance.setText("Total distance:  " + result);
    }
}
