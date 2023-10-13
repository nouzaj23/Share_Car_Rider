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

public class TemplatesPanel extends AbstractPanel<Ride> {

    private final JTable table;
    private final Action addAction = new AddAction<Ride>(this);
    public TemplatesPanel() {
        setLayout(new BorderLayout());
        table = setUpTable();
        var toolbar = new JToolBar();
        toolbar.add(addAction);

        add(toolbar, BorderLayout.SOUTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private JTable setUpTable() {
        return super.setUpTable(new String[]{"Name","Passengers", "Currency", "Category", "From", "To", "Distance"},
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
