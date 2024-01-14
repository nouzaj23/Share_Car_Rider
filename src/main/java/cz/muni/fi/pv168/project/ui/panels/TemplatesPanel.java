package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.actions.DeleteAction;
import cz.muni.fi.pv168.project.ui.actions.EditAction;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.dialog.TemplateDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryListModel;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;
import cz.muni.fi.pv168.project.ui.model.CurrencyListModel;
import cz.muni.fi.pv168.project.ui.model.TemplateModel;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;
import cz.muni.fi.pv168.project.ui.panels.helper.PopupMenuGenerator;
import cz.muni.fi.pv168.project.ui.renderers.CategoryRenderer;
import cz.muni.fi.pv168.project.ui.renderers.CurrencyRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TemplatesPanel extends AbstractPanel<Template> {

    private final TemplateModel templateModel;
    private final CategoryListModel categoryListModel;
    private final CurrencyListModel currencyListModel;
    private final Validator<Template> templateValidator;
    private Action addAction;
    private Action editAction;
    private Action deleteAction;

    public TemplatesPanel(TemplateModel templateModel,
                          CategoryListModel categoryListModel,
                          CurrencyListModel currencyListModel,
                          Validator<Template> templateValidator) {
        this.templateModel = templateModel;
        this.categoryListModel = categoryListModel;
        this.currencyListModel = currencyListModel;
        this.templateValidator = templateValidator;

        setLayout(new BorderLayout());

        this.addAction = new AddAction<>(this);
        this.editAction = new EditAction<>(this);
        this.deleteAction = new DeleteAction<>(this);
        var actions = Arrays.asList(addAction, editAction, deleteAction);
        addAction.setEnabled(true);
        editAction.setEnabled(false);
        deleteAction.setEnabled(false);

        this.table = setUpTable(actions);

        var tableEmptySpaceClickAction = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON3) {
                    PopupMenuGenerator.generatePopupMenu(TemplatesPanel.this, actions).show(me.getComponent(), me.getX(), me.getY());
                }
            }
        };
        PanelHelper.createTopBar(this, table, null, null, Optional.of(tableEmptySpaceClickAction), actions);
    }

    private JTable setUpTable(List<Action> actions) {
        var table = new JTable(templateModel);

        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        var currencyComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(currencyListModel));
        currencyComboBox.setRenderer(new CurrencyRenderer());
        table.setDefaultEditor(Currency.class, new DefaultCellEditor(currencyComboBox));
        var categoryComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        categoryComboBox.setRenderer(new CategoryRenderer());
        table.setDefaultEditor(Category.class, new DefaultCellEditor(categoryComboBox));
        table.setComponentPopupMenu(PopupMenuGenerator.generatePopupMenu(this, actions));
        table.setDefaultRenderer(Currency.class, new CurrencyRenderer());
        table.setDefaultRenderer(Category.class, new CategoryRenderer());

        return table;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var selectedCount = selectionModel.getSelectedItemsCount();

        if (selectedCount == 0) {
            addAction.setEnabled(true);
            editAction.setEnabled(false);
            deleteAction.setEnabled(false);
        } else if (selectedCount == 1) {
            addAction.setEnabled(true);
            editAction.setEnabled(true);
            deleteAction.setEnabled(true);
        } else {
            addAction.setEnabled(true);
            editAction.setEnabled(false);
            deleteAction.setEnabled(true);
        }
    }

    @Override
    public EntityDialog<Template> getDialog() {
        return new TemplateDialog(
            new Template(GuidProvider.newGuid(), "", 0, null, null, "", "", 0),
            categoryListModel,
            currencyListModel, templateValidator
        );
    }

    @Override
    public EntityDialog<Template> getDialog(Template entity) {
        return new TemplateDialog(entity, categoryListModel, currencyListModel, templateValidator);
    }

    @Override
    public EntityDialog<Template> getEditDialog(Template entity) {
        return getDialog(entity);
    }

    @Override
    public void addRow(Template entity) {
        templateModel.addRow(entity);
    }

    @Override
    public void deleteRow(int rowIndex) {
        templateModel.deleteRow(rowIndex);
    }

    @Override
    public void editRow(Template newEntity, Ride oldRide) {
        templateModel.updateRow(newEntity);
    }
}
