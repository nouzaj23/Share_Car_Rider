package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.renderers.TemplateRenderer;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.time.LocalDate;

public class RideDialog extends EntityDialog<Ride>{
    private final JTextField name = new JTextField();
    private final JSpinner passengers = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final ComboBoxModel<Currency> currencyModel = new DefaultComboBoxModel<>(Currency.values());
    private final JComboBox<Category> categoryJComboBox;
    private final JTextField from = new JTextField();
    private final JTextField to = new JTextField();
    private final JSpinner distance = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final JSpinner hours = new JSpinner(new SpinnerNumberModel(1, 0, Float.MAX_VALUE, 0.1));
    private final DateModel<LocalDate> date = new LocalDateModel();
    private final Ride ride;
    private final JComboBox<Ride> templates;
    private final CategoryModel categoryModel;

    public RideDialog(Ride ride, ListModel<Category> categoryListModel, TemplateModel templateModel, CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
        this.ride = ride;
        this.categoryJComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        this.categoryJComboBox.setEditable(true);
        this.templates = new JComboBox<>(new DefaultComboBoxModel<>(templateModel.getArray()));
        templates.setSelectedItem(null);
        templates.setRenderer(new TemplateRenderer());
        templates.addActionListener(e -> {
            Ride selectedTemplate = (Ride) templates.getSelectedItem();

            if (selectedTemplate != null) {
                name.setText(selectedTemplate.getName());
                passengers.setValue(selectedTemplate.getPassengers());
                currencyModel.setSelectedItem(selectedTemplate.getCurrency());
                categoryJComboBox.setSelectedItem(selectedTemplate.getCategory());
                from.setText(selectedTemplate.getFrom());
                to.setText(selectedTemplate.getTo());
                distance.setValue(selectedTemplate.getDistance());
                hours.setValue(selectedTemplate.getHours());
                date.setValue(selectedTemplate.getDate());
            }
        });

        setValues();
        addFields();

        JButton addTemplate = new JButton("Add Template");
        addTemplate.addActionListener(e -> {
            templateModel.addRow(getEntity());
        });

        addButton(addTemplate);
    }
    private void setValues() {
        name.setText(ride.getName());
        passengers.setValue(ride.getPassengers());
        currencyModel.setSelectedItem(ride.getCurrency());
        categoryJComboBox.setSelectedItem(ride.getCategory());
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
        add("Currency:", new JComboBox<>(currencyModel));
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
        ride.setCurrency((Currency) currencyModel.getSelectedItem());
        if (!(categoryJComboBox.getSelectedItem() instanceof Category)) {
            Category newCategory = new Category(categoryJComboBox.getSelectedItem().toString());
            categoryModel.addRow(newCategory);
            ride.setCategory(newCategory);
        } else {
            ride.setCategory((Category) categoryJComboBox.getSelectedItem());
        }
        ride.setFrom(from.getText());
        ride.setTo(to.getText());
        ride.setDistance(((Number) distance.getValue()).intValue());
        ride.setHours(((Number) hours.getValue()).floatValue());
        ride.setDate(date.getValue());
        return ride;
    }
}
