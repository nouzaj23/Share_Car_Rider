package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TemplateModel extends AbstractTableModel implements EntityTableModel<Template> {

    private  List<Template> templates;
    private final CrudService<Template> templateCrudService;
    private final List<Column<Template, ?>> columns = List.of(
            Column.editable("Name", String.class, Template::getName, Template::setName),
            Column.editable("Passengers", Integer.class, Template::getPassengers, Template::setPassengers),
            Column.editable("Currency", Currency.class, Template::getCurrency, Template::setCurrency),
            Column.editable("Category", Category.class, Template::getCategory, Template::setCategory),
            Column.readonly("From", String.class, Template::getFrom),
            Column.readonly("To", String.class, Template::getTo),
            Column.editable("Distance", Integer.class, Template::getDistance, Template::setDistance),
            Column.editable("Hours", Float.class, Template::getHours, Template::setHours)
    );

    public TemplateModel(CrudService<Template> templateCrudService) {
        this.templateCrudService = templateCrudService;
        this.templates = templateCrudService.findAll();
    }
    public Template[] getArray(){
        return templates.toArray(Template[]::new);
    }
    @Override
    public int getRowCount() {
        return templates.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var template = getEntity(rowIndex);
        return columns.get(columnIndex).getValue((template));
    }

    @Override
    public Template getEntity(int rowIndex) {
        return templates.get(rowIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value != null) {
            var template = getEntity(rowIndex);
            columns.get(columnIndex).setValue(value, template);
            updateRow(template);
        }
    }

    public void deleteRow(int rowIndex) {
        var templateToBeDeleted = getEntity(rowIndex);
        templateCrudService.deleteByGuid(templateToBeDeleted.getGuid());
        templates.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Template template) {
        templateCrudService.create(template)
                .intoException();
        int newRowIndex = templates.size();
        templates.add(template);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Template template) {
        templateCrudService.update(template)
                .intoException();
        int rowIndex = templates.indexOf(template);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void refresh() {
        this.templates = new ArrayList<>(templateCrudService.findAll());
        fireTableDataChanged();
    }
}
