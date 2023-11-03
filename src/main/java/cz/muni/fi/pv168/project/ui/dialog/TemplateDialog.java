package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;

import javax.swing.*;

public class TemplateDialog extends EntityDialog<Ride>{
    private final JTextField name = new JTextField();
    private final JSpinner passengers = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final ComboBoxModel<Currency> currencyModel = new DefaultComboBoxModel<>(Currency.values());
    private final JComboBox<Category> categoryModel;
    private final JTextField from = new JTextField();
    private final JTextField to = new JTextField();
    private final JSpinner distance = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final JSpinner hours = new JSpinner(new SpinnerNumberModel(1, 0, Float.MAX_VALUE, 0.1));
    private final Ride ride;

    public TemplateDialog(Ride ride, ListModel<Category> categoryListModel) {
        this.ride = ride;
        this.categoryModel = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        setValues();
        addFields();
    }
    private void setValues() {
        name.setText(ride.getName());
        passengers.setValue(ride.getPassengers());
        currencyModel.setSelectedItem(ride.getCurrency());
        categoryModel.setSelectedItem(ride.getCategory());
        from.setText(ride.getFrom());
        to.setText(ride.getTo());
        distance.setValue(ride.getDistance());
        hours.setValue(ride.getHours());
    }

    private void addFields() {
        add("Template name:", name);
        add("Number of passengers:", passengers);
        add("Currency:", new JComboBox<>(currencyModel));
        add("Category:", categoryModel);
        add("From:", from);
        add("To:", to);
        add("Distance (km): ", distance);
        add("Hours:", hours);
    }

    @Override
    Ride getEntity() {
        ride.setName(name.getText());
        ride.setPassengers(((Number) passengers.getValue()).intValue());
        ride.setCurrency((Currency) currencyModel.getSelectedItem());
        ride.setCategory((Category) categoryModel.getSelectedItem());
        ride.setFrom(from.getText());
        ride.setTo(to.getText());
        ride.setDistance(((Number) distance.getValue()).intValue());
        ride.setHours(((Number) hours.getValue()).floatValue());
        ride.setDate(null);
        return ride;
    }
}
