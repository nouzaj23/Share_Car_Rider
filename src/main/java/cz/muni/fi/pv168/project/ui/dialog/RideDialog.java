package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;

import javax.swing.*;
import java.time.LocalDateTime;

public class RideDialog extends EntityDialog<Ride>{
    private final JTextField name = new JTextField();
    private final JTextField passengers = new JTextField();
    private final JTextField currency = new JTextField();
    private final JTextField category = new JTextField();
    private final JTextField from = new JTextField();
    private final JTextField to = new JTextField();
    private final JTextField distance = new JTextField();

    private final Ride ride;

    public RideDialog(Ride ride) {
        this.ride = ride;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(ride.getName());
        passengers.setText("");
        currency.setText("");
        category.setText("");
        from.setText("");
        to.setText("");
        distance.setText("");
    }

    private void addFields() {
        add("Ride name:", name);
        add("Number of passengers:", passengers);
        add("Currency:", currency);
        add("Currency:", category);
        add("Date From:", from);
        add("Date to:", to);
        add("Distance", distance);
    }

    @Override
    Ride getEntity() {
        ride.setName(name.getText());
        ride.setPassengers(Integer.parseInt(passengers.getText()));
        ride.setCurrency(Currency.CZK);
        ride.setCategory(new Category("Sluzobka"));
        ride.setFrom(LocalDateTime.now());
        ride.setTo(LocalDateTime.now());
        ride.setDistance(100);
        return ride;
    }
}
