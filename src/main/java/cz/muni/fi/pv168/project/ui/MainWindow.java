package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.export.CSVexport;
import cz.muni.fi.pv168.project.export.JsonExport;
import cz.muni.fi.pv168.project.export.service.GenericExportService;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.storage.sql.TransactionalExportService;
import cz.muni.fi.pv168.project.ui.actions.DarkModeToggle;
import cz.muni.fi.pv168.project.ui.actions.ExportAction;
import cz.muni.fi.pv168.project.ui.actions.ImportAction;
import cz.muni.fi.pv168.project.ui.misc.HelpAboutPopup;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;
import cz.muni.fi.pv168.project.ui.panels.CategoriesPanel;
import cz.muni.fi.pv168.project.ui.panels.CurrencyPanel;
import cz.muni.fi.pv168.project.ui.panels.TemplatesPanel;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow {

    private JFrame frame;
    private final TemplateModel templateModel;
    private final CategoryModel categoryModel;
    private final CarRidesModel carRideModel;
    private final CurrencyModel currencyModel;
    private final CategoriesPanel categoriesPanel;
    private final CarRidesPanel carRidesPanel;
    private final TemplatesPanel templatesPanel;
    private final CurrencyPanel currencyPanel;
    private final CurrencyListModel currencyListModel;
    private final CategoryListModel categoryListModel;


    public MainWindow(DependencyProvider dependencyProvider) {
        initializeFrame();

        this.templateModel = new TemplateModel(dependencyProvider.getTemplateCrudService());
        var categoryListModel = new CategoryListModel(dependencyProvider.getCategoryCrudService());
        this.categoryModel = new CategoryModel(dependencyProvider.getCategoryCrudService(), categoryListModel);
        this.carRideModel = new CarRidesModel(dependencyProvider.getRideCrudService(), categoryModel);
        categoryModel.setRidesModel(carRideModel);
        var currencyListModel = new CurrencyListModel(dependencyProvider.getCurrencyCrudService());
        this.currencyModel = new CurrencyModel(dependencyProvider.getCurrencyCrudService(), currencyListModel);

        var categoryValidator = dependencyProvider.getCategoryValidator();
        var templateValidator = dependencyProvider.getTemplateValidator();
        var currencyValidator = dependencyProvider.getCurrencyValidator();
        var rideValidator = dependencyProvider.getRideValidator();

        this.carRidesPanel = createCarRidesPanel(carRideModel, categoryListModel, templateModel, categoryModel, currencyListModel, rideValidator);
        this.categoriesPanel = createCategoriesPanel(categoryModel, categoryValidator);
        this.templatesPanel = createTemplatesPanel(templateModel, categoryListModel, currencyListModel, templateValidator);
        this.currencyPanel = createCurrencyPanel(currencyModel, currencyValidator, carRideModel);

        var tabbedPane = createTabbedPane(carRidesPanel, categoriesPanel, templatesPanel, currencyPanel);

        frame.setJMenuBar(createMenuBar(dependencyProvider));
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.pack();

        this.categoryListModel = categoryListModel;
        this.currencyListModel = currencyListModel;
    }

    private void initializeFrame() {
        frame = new JFrame("Share Car Rider");
        frame.setSize(1024, 1024);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JMenuBar createMenuBar(DependencyProvider dependencyProvider) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Menu");
        JMenuItem darkModeToggle = new JCheckBoxMenuItem(new DarkModeToggle(frame));
        JMenuItem exportItem = new JMenuItem(new ExportAction(frame,
                new TransactionalExportService (new GenericExportService(carRidesPanel, templatesPanel, categoriesPanel,
                                                                      carRideModel, templateModel, categoryModel,
                                                                      dependencyProvider,
                                                                      List.of( new JsonExport(), new CSVexport())), dependencyProvider.getTransactionExecutor())));
        JMenuItem importItem = new JMenuItem(new ImportAction(frame, dependencyProvider.getImportService(), this::refresh));
        fileMenu.add(exportItem);
        fileMenu.add(importItem);

        fileMenu.add(darkModeToggle);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new HelpAboutPopup("About");
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private CarRidesPanel createCarRidesPanel(CarRidesModel carRideModel, CategoryListModel categoryListModel, TemplateModel templateModel, CategoryModel categoryModel, CurrencyListModel currencyListModel, Validator<Ride> rideValidator) {
        return new CarRidesPanel(carRideModel, categoryListModel, templateModel, categoryModel, currencyListModel, rideValidator);
    }

    private CategoriesPanel createCategoriesPanel(CategoryModel categoryModel, Validator<Category> categoryValidator) {
        return new CategoriesPanel(categoryModel, categoryValidator);
    }

    private TemplatesPanel createTemplatesPanel(TemplateModel templateModel, CategoryListModel categoryListModel, CurrencyListModel currencyListModel, Validator<Template> templateValidator) {
        return new TemplatesPanel(templateModel, categoryListModel, currencyListModel, templateValidator);
    }

    private CurrencyPanel createCurrencyPanel(CurrencyModel currencyModel, Validator<Currency> currencyValidator, CarRidesModel rideModel) {
        return new CurrencyPanel(currencyModel,currencyValidator, rideModel);
    }

    private JTabbedPane createTabbedPane(CarRidesPanel carRidesPanel, CategoriesPanel categoriesPanel, TemplatesPanel templatesPanel, CurrencyPanel currencyPanel) {
        var tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Car Rides", carRidesPanel);
        tabbedPane.addTab("Categories", categoriesPanel);
        tabbedPane.addTab("Templates", templatesPanel);
        tabbedPane.addTab("Currency", currencyPanel);
        return tabbedPane;
    }

    public void show() {
        frame.setVisible(true);
    }

    private void refresh() {
        carRideModel.refresh();
        templateModel.refresh();
        categoryModel.refresh();
        currencyModel.refresh();
        categoryListModel.refresh();
        currencyListModel.refresh();
    }
}
