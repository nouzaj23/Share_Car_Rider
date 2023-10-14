package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;

import javax.swing.*;
import java.time.LocalDateTime;

public class RideDialog extends EntityDialog<Ride>{
    private final JTextField name = new JTextField();
    private final JSpinner passengers = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final ComboBoxModel<Currency> currencyModel = new DefaultComboBoxModel<>(Currency.values());
    private final ComboBoxModel<Category> categoryListModel;
    private final JTextField from = new JTextField();
    private final JTextField to = new JTextField();
    private final JSpinner distance = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

    private final Ride ride;

    public RideDialog(Ride ride, ListModel<Category> categoryListModel) {
        this.ride = ride;
        this.categoryListModel = new ComboBoxModelAdapter<>(categoryListModel);
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(ride.getName());
        passengers.setValue(ride.getPassengers());
        currencyModel.setSelectedItem(ride.getCurrency());
        categoryListModel.setSelectedItem(ride.getCategory());
        from.setText(LocalDateTime.now().toString());
        to.setText(LocalDateTime.now().plusHours(2).toString());
        distance.setValue(ride.getDistance());
    }

    private void addFields() {
        add("Ride name:", name);
        add("Number of passengers:", passengers);
        add("Currency:", new JComboBox<>(currencyModel));
        add("Category:", new JComboBox<>(categoryListModel));
        add("Date From:", from);
        add("Date to:", to);
        add("Distance", distance);
    }

    @Override
    Ride getEntity() {
        ride.setName(name.getText());
        ride.setPassengers(((Number) passengers.getValue()).intValue());
        ride.setCurrency((Currency) currencyModel.getSelectedItem());
        ride.setCategory((Category) categoryListModel.getSelectedItem());
        ride.setFrom(LocalDateTime.parse(from.getText()));
        ride.setTo(LocalDateTime.parse(to.getText()));
        ride.setDistance(((Number) distance.getValue()).intValue());
        return ride;
    }
}
