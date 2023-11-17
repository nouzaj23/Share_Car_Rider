package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.TestDataGenerator;
import cz.muni.fi.pv168.project.export.CSVexport;
import cz.muni.fi.pv168.project.export.JsonExport;
import cz.muni.fi.pv168.project.export.service.ExportService;
import cz.muni.fi.pv168.project.export.service.GenericExportService;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.ui.actions.DarkModeToggle;
import cz.muni.fi.pv168.project.ui.actions.ExportAction;
import cz.muni.fi.pv168.project.ui.misc.HelpAboutPopup;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;
import cz.muni.fi.pv168.project.ui.panels.CategoriesPanel;
import cz.muni.fi.pv168.project.ui.panels.TemplatesPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWindow {

    private JFrame frame;

    private ExportService exportService;

    public MainWindow() {
        initializeFrame();

        var templateModel = new TemplateModel(new ArrayList<>());
        var categoryListModel = new CategoryListModel(TestDataGenerator.CATEGORIES);
        var categoryModel = new CategoryModel(categoryListModel);
        var carRideModel = new CarRidesModel(new ArrayList<>());
        var currencyListModel = new CurrencyListModel(Arrays.stream(Currency.values()).toList());

        var carRidesPanel = createCarRidesPanel(carRideModel, categoryListModel, templateModel, categoryModel, currencyListModel);
        var categoriesPanel = createCategoriesPanel(categoryModel);
        var templatesPanel = createTemplatesPanel(templateModel, categoryListModel);

        var tabbedPane = createTabbedPane(carRidesPanel, categoriesPanel, templatesPanel);

        this.exportService = new GenericExportService(carRideModel, templateModel, categoryModel, List.of(new CSVexport(), new JsonExport()));
        frame.setJMenuBar(createMenuBar(exportService));
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.pack();
    }

    private void initializeFrame() {
        frame = new JFrame("Share Car Rider");
        frame.setSize(1024, 1024);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JMenuBar createMenuBar(ExportService exportService) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenuItem darkModeToggle = new JCheckBoxMenuItem(new DarkModeToggle(frame));
        JMenuItem exportMenuItem = new JMenuItem(new ExportAction(frame, exportService));
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        fileMenu.add(darkModeToggle);
        fileMenu.add(exportMenuItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new HelpAboutPopup("About");
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private CarRidesPanel createCarRidesPanel(CarRidesModel carRideModel, CategoryListModel categoryListModel, TemplateModel templateModel, CategoryModel categoryModel, CurrencyListModel currencyListModel) {
        return new CarRidesPanel(carRideModel, categoryListModel, this::changeActionsState, templateModel, categoryModel, currencyListModel);
    }

    private CategoriesPanel createCategoriesPanel(CategoryModel categoryModel) {
        return new CategoriesPanel(categoryModel, this::changeActionsState);
    }

    private TemplatesPanel createTemplatesPanel(TemplateModel templateModel, CategoryListModel categoryListModel) {
        return new TemplatesPanel(templateModel, categoryListModel, this::changeActionsState);
    }

    private JTabbedPane createTabbedPane(CarRidesPanel carRidesPanel, CategoriesPanel categoriesPanel, TemplatesPanel templatesPanel) {
        var tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Car Rides", carRidesPanel);
        tabbedPane.addTab("Categories", categoriesPanel);
        tabbedPane.addTab("Templates", templatesPanel);
        return tabbedPane;
    }

    public void show() {
        frame.setVisible(true);
    }

    private void changeActionsState(int selectedItemsCount) {
    }
}
