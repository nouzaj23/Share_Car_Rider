package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.*;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.renderers.TemplateRenderer;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.time.LocalDate;

public class RideDialog extends EntityDialog<Ride>{
    private final JTextField name = new JTextField();
    private final JSpinner passengers = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final JComboBox<Currency> currencyJComboBox;
    private final JSpinner fuelExpenses = new JSpinner(new SpinnerNumberModel(1, - Float.MAX_VALUE, Float.MAX_VALUE, 0.1));
    private final JComboBox<Category> categoryJComboBox;
    private final JTextField from = new JTextField();
    private final JTextField to = new JTextField();
    private final JSpinner distance = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final JSpinner hours = new JSpinner(new SpinnerNumberModel(1, 0, Float.MAX_VALUE, 0.1));
    private final DateModel<LocalDate> date = new LocalDateModel();
    private final Ride ride;
    private final JComboBox<Template> templates;
    private final CategoryModel categoryModel;

    public RideDialog(Ride ride, ListModel<Category> categoryListModel, ListModel<Currency> currencyListModel, TemplateModel templateModel, CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
        this.ride = ride;
        this.currencyJComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(currencyListModel));
        this.currencyJComboBox.setEditable(true);
        this.categoryJComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        this.categoryJComboBox.setEditable(true);
        this.templates = new JComboBox<>(new DefaultComboBoxModel<>(templateModel.getArray()));
        templates.setSelectedItem(null);
        templates.setRenderer(new TemplateRenderer());
        templates.addActionListener(e -> {
            AbstractRide selectedTemplate = (AbstractRide) templates.getSelectedItem();

            if (selectedTemplate != null) {
                name.setText(selectedTemplate.getName());
                passengers.setValue(selectedTemplate.getPassengers());
                currencyJComboBox.setSelectedItem(selectedTemplate.getCurrency());
                categoryJComboBox.setSelectedItem(selectedTemplate.getCategory());
                from.setText(selectedTemplate.getFrom());
                to.setText(selectedTemplate.getTo());
                distance.setValue(selectedTemplate.getDistance());
                hours.setValue(selectedTemplate.getHours());
            }
        });

        setValues();
        addFields();

        JButton addTemplate = new JButton("Add Template");
        addTemplate.addActionListener(e -> {
            templateModel.addRow(getEntity().extractTemplate());
        });

        addButton(addTemplate);
    }
    private void setValues() {
        name.setText(ride.getName());
        passengers.setValue(ride.getPassengers());
        currencyJComboBox.setSelectedItem(ride.getCurrency());
        categoryJComboBox.setSelectedItem(ride.getCategory());
        fuelExpenses.setValue(ride.getFuelExpenses());
        from.setText(ride.getFrom());
        to.setText(ride.getTo());
        distance.setValue(ride.getDistance());
        hours.setValue(ride.getHours());
        date.setValue(ride.getDate());
    }

    private void addFields() {
        add("Templates", templates);
        add("Ride name:", name);
        add("Number of passengers:", passengers);
        add("Currency:", currencyJComboBox);
        add("Fuel Expenses:", fuelExpenses);
        add("Category:", categoryJComboBox);
        add("From:", from);
        add("To:", to);
        add("Distance (km): ", distance);
        add("Hours:", hours);
        add("Date", new JDatePicker(date));
    }

    @Override
    Ride getEntity() {
        ride.setName(name.getText());
        ride.setPassengers(((Number) passengers.getValue()).intValue());
        ride.setCurrency((Currency) currencyJComboBox.getSelectedItem());
        if (!(categoryJComboBox.getSelectedItem() instanceof Category)) {
            Category newCategory = new Category(GuidProvider.newGuid(), categoryJComboBox.getSelectedItem().toString());
            categoryModel.addRow(newCategory);
            ride.setCategory(newCategory);
        } else {
            ride.setCategory((Category) categoryJComboBox.getSelectedItem());
        }
        ride.setFrom(from.getText());
        ride.setTo(to.getText());
        ride.setDistance(((Number) distance.getValue()).intValue());
        ride.setHours(((Number) hours.getValue()).floatValue());
        ride.setFuelExpenses(((Number) fuelExpenses.getValue()).floatValue());
        ride.setDate(date.getValue());
        return ride;
    }
}
