package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.*;
import cz.muni.fi.pv168.project.business.service.validation.ValidationException;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.editors.CategoryComboBoxEditor;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.renderers.CategoryRenderer;
import cz.muni.fi.pv168.project.ui.renderers.CurrencyRenderer;
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

    public RideDialog(Ride ride,
                      ListModel<Category> categoryListModel,
                      ListModel<Currency> currencyListModel,
                      TemplateModel templateModel,
                      CategoryModel categoryModel,
                      Validator<Ride> rideValidator) {
        super(rideValidator);
        this.categoryModel = categoryModel;
        this.ride = ride;
        this.currencyJComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(currencyListModel));
        this.currencyJComboBox.setEditable(false);
        this.currencyJComboBox.setRenderer(new CurrencyRenderer());
        this.categoryJComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        this.categoryJComboBox.setEditor(new CategoryComboBoxEditor());
        this.categoryJComboBox.setRenderer(new CategoryRenderer());
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

        JButton addTemplate = saveAsTemplate(templateModel);
        addButton(addTemplate);
    }

    private JButton saveAsTemplate(TemplateModel templateModel) {
        JButton addTemplate = new JButton("Save as Template");
        addTemplate.addActionListener(e -> {
            var template = getEntity().extractTemplate();
            var isValid = true;
            try {
                templateModel.addRow(template);
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        String.join("\n", ex.getValidationErrors()),
                        "Template creation failed",
                        JOptionPane.ERROR_MESSAGE);
                isValid = false;
            }
            if (isValid) {
                JOptionPane.showMessageDialog(
                        this,
                        "New template %s successfully created".formatted(template.getName()),
                        "Template creation succeeded",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        return addTemplate;
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
        addErrorPanel();
    }

    @Override
    Ride getEntity() {
        ride.setName(name.getText());
        ride.setPassengers(((Number) passengers.getValue()).intValue());
        ride.setCurrency((Currency) currencyJComboBox.getSelectedItem());
        var category = categoryJComboBox.getSelectedItem();
        if (!(category instanceof Category)) {
            if (category != null) {
                Category newCategory = new Category(GuidProvider.newGuid(), category.toString());
                categoryModel.addRow(newCategory);
                ride.setCategory(newCategory);
            }
        } else {
            ride.setCategory((Category) category);
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
