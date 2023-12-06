package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;

import javax.swing.*;

public class TemplateDialog extends EntityDialog<Template>{
    private final JTextField name = new JTextField();
    private final JSpinner passengers = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final JComboBox<Currency> currencyModel;
    private final JComboBox<Category> categoryModel;
    private final JTextField from = new JTextField();
    private final JTextField to = new JTextField();
    private final JSpinner distance = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final JSpinner hours = new JSpinner(new SpinnerNumberModel(1, 0, Float.MAX_VALUE, 0.1));
    private final Template template;

    public TemplateDialog(Template template,
                          ListModel<Category> categoryListModel,
                          ListModel<Currency> currencyListModel,
                          Validator<Template> templateValidator) {
        super(templateValidator);
        this.template = template;
        this.currencyModel = new JComboBox<>(new ComboBoxModelAdapter<>(currencyListModel));
        this.categoryModel = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        setValues();
        addFields();
    }
    private void setValues() {
        name.setText(template.getName());
        passengers.setValue(template.getPassengers());
        currencyModel.setSelectedItem(template.getCurrency());
        categoryModel.setSelectedItem(template.getCategory());
        from.setText(template.getFrom());
        to.setText(template.getTo());
        distance.setValue(template.getDistance());
        hours.setValue(template.getHours());
    }

    private void addFields() {
        add("Template name:", name);
        add("Number of passengers:", passengers);
        add("Currency:", currencyModel);
        add("Category:", categoryModel);
        add("From:", from);
        add("To:", to);
        add("Distance (km): ", distance);
        add("Hours:", hours);
    }

    @Override
    Template getEntity() {
        template.setName(name.getText());
        template.setPassengers(((Number) passengers.getValue()).intValue());
        template.setCurrency((Currency) currencyModel.getSelectedItem());
        template.setCategory((Category) categoryModel.getSelectedItem());
        template.setFrom(from.getText());
        template.setTo(to.getText());
        template.setDistance(((Number) distance.getValue()).intValue());
        template.setHours(((Number) hours.getValue()).floatValue());
        return template;
    }
}
