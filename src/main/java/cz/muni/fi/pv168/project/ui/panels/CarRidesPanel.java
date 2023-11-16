package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.dialog.RideDialog;
import cz.muni.fi.pv168.project.ui.filters.RideTableFilter;
import cz.muni.fi.pv168.project.ui.filters.components.FilterListModelBuilder;
import cz.muni.fi.pv168.project.ui.filters.components.FilterTextModelBuilder;
import cz.muni.fi.pv168.project.ui.filters.components.FilterValueRangeModelBuilder;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;
import cz.muni.fi.pv168.project.ui.panels.helper.PopupMenuGenerator;
import cz.muni.fi.pv168.project.ui.renderers.CategoryRenderer;
import cz.muni.fi.pv168.project.ui.renderers.CurrencyRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CarRidesPanel extends AbstractPanel<Ride> {

    private final Consumer<Integer> onSelectionChange;
    private final CarRidesModel carRidesModel;
    private final CategoryListModel categoryListModel;
    private final JLabel totalFuelExpenses = new JLabel();
    private final JLabel filteredFuelExpenses = new JLabel();
    private final TemplateModel templates;
    private final CategoryModel categoryModel;
    private final CurrencyListModel currencyListModel;

    public CarRidesPanel(CarRidesModel carRidesModel, CategoryListModel categoryListModel,
                         Consumer<Integer> onSelectionChange, TemplateModel templates,
                         CategoryModel categoryModel, CurrencyListModel currencyListModel) {

        this.carRidesModel = carRidesModel;
        this.carRidesModel.setLinkedPannel(this);
        this.categoryListModel = categoryListModel;
        this.currencyListModel = currencyListModel;
        this.categoryModel = categoryModel;
        this.templates = templates;
        this.onSelectionChange = onSelectionChange;
        setLayout(new BorderLayout());

        var toolbar = new JToolBar();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

        var rowSorter = new TableRowSorter<>(carRidesModel);
        var carRideFilter = new RideTableFilter(rowSorter);

        this.table = setUpTable();
        table.setRowSorter(rowSorter);

        JPanel statsPanel = new JPanel(new GridBagLayout());
        setUpStatsPanel(statsPanel);
        triggerStatsUpdate();
        PanelHelper.createTopBar(this, table, createFilterPanel(carRideFilter), statsPanel);
    }

    private void setUpStatsPanel(JPanel statsPanel) {
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Aligns contents to the left
        totalFuelExpenses.setFont(new Font("Arial", Font.BOLD, 14));
        filteredFuelExpenses.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.setBackground(new Color(230, 230, 250)); // Light background
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        statsPanel.add(totalFuelExpenses);
        statsPanel.add(filteredFuelExpenses);
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
        triggerStatsUpdate();
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
        triggerStatsUpdate();
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
        triggerStatsUpdate();
    }

    public List<Ride> getFilteredRides() {
        List<Ride> filteredRides = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            int modelIndex = table.convertRowIndexToModel(i);
            Ride ride = carRidesModel.getEntity(modelIndex);
            filteredRides.add(ride);
        }
        return filteredRides;
    }
    
    public void triggerStatsUpdate() {
        triggerAllRides();
        triggerFilteredRides();
    }
    
    private void triggerAllRides() {
        float result = 0;
        for (int i = 0; i < carRidesModel.getRowCount(); i++) {
            Ride ride = carRidesModel.getEntity(i);
            result += ride.getFuelExpenses();
        }
        totalFuelExpenses.setText("Total Fuel Expenses:  " + result);
    }
    
    private void triggerFilteredRides() {
        float result = 0;
        List<Ride> filteredRides = getFilteredRides();
        for (Ride ride: filteredRides) {
            result += ride.getFuelExpenses();
        }
        filteredFuelExpenses.setText("Filtered Fuel Expenses:  " + result);
    }

    private static JList<Category> createCategoryFilter(
            RideTableFilter rideTableFilter, CategoryListModel categoryListModel) {
        return FilterListModelBuilder.create(categoryListModel)
                .setVisibleRowsCount(3)
                .setValuesRenderer(new CategoryRenderer())
                .setFilter(rideTableFilter::filterCategory)
                .build();
    }

    private static JList<Currency> createCurrencyFilter(
            RideTableFilter rideTableFilter, CurrencyListModel currencyListModel) {
        return FilterListModelBuilder.create(currencyListModel)
                .setVisibleRowsCount(3)
                .setValuesRenderer(new CurrencyRenderer())
                .setFilter(rideTableFilter::filterCurrency)
                .build();
    }

    private static JTextField createFromFilter(
            RideTableFilter rideTableFilter) {
        return FilterTextModelBuilder.create()
                .setFilter(rideTableFilter::filterFrom)
                .build();
    }

    private static JTextField createToFilter(
            RideTableFilter rideTableFilter) {
        return FilterTextModelBuilder.create()
                .setFilter(rideTableFilter::filterTo)
                .build();
    }

    private static JTextField createPassengerFilterMin(
            RideTableFilter rideTableFilter) {
        return FilterValueRangeModelBuilder.create()
                .setFilter(rideTableFilter::filterMin)
                .build();
    }

    private static JTextField createPassengerFilterMax(
            RideTableFilter rideTableFilter) {
        return FilterValueRangeModelBuilder.create()
                .setFilter(rideTableFilter::filterMax)
                .build();
    }

    private JPanel createFilterPanel(RideTableFilter carRideFilter) {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        JLabel categoryLabel = new JLabel("Category:");
        JLabel currencyLabel = new JLabel("Currency:");
        JLabel fromLabel = new JLabel("From:");
        JLabel toLabel = new JLabel("To:");
        JLabel passengersLabelMin = new JLabel("Min passenger count:");
        JLabel passengersLabelMax = new JLabel("Max passenger count:");

        var categoryFilter = createCategoryFilter(carRideFilter, categoryListModel);
        var currencyFilter = createCurrencyFilter(carRideFilter, currencyListModel);
        var fromFilter = createFromFilter(carRideFilter);
        var toFilter = createToFilter(carRideFilter);
        var passengerFilterMin = createPassengerFilterMin(carRideFilter);
        var passengerFilterMax = createPassengerFilterMax(carRideFilter);

        filterPanel.add(categoryLabel);
        filterPanel.add(new JScrollPane(categoryFilter));

        filterPanel.add(currencyLabel);
        filterPanel.add(new JScrollPane(currencyFilter));

        JPanel passengerFilterPanel = new JPanel();
        passengerFilterPanel.setLayout(new BoxLayout(passengerFilterPanel, BoxLayout.Y_AXIS));

        passengerFilterPanel.add(passengersLabelMin);
        passengerFilterPanel.add(passengerFilterMin);

        passengerFilterPanel.add(passengersLabelMax);
        passengerFilterPanel.add(passengerFilterMax);

        filterPanel.add(passengerFilterPanel);

        JPanel fromToFilterPanel = new JPanel();
        fromToFilterPanel.setLayout(new BoxLayout(fromToFilterPanel, BoxLayout.Y_AXIS));
        fromToFilterPanel.add(fromLabel);
        fromToFilterPanel.add(fromFilter);

        fromToFilterPanel.add(toLabel);
        fromToFilterPanel.add(toFilter);

        filterPanel.add(fromToFilterPanel);

        var resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            carRideFilter.resetFilter();
            currencyFilter.setSelectedIndices(new int[0]);
            categoryFilter.setSelectedIndices(new int[0]);
            passengerFilterMin.setText("");
            passengerFilterMax.setText("");
            fromFilter.setText("");
            toFilter.setText("");
        });
        filterPanel.add(resetButton);

        return filterPanel;
    }
}
