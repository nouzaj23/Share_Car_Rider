package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.actions.DarkModeToggle;
import cz.muni.fi.pv168.project.ui.misc.HelpAboutPopup;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;
import cz.muni.fi.pv168.project.ui.panels.CategoriesPanel;
import cz.muni.fi.pv168.project.ui.panels.CurrencyPanel;
import cz.muni.fi.pv168.project.ui.panels.TemplatesPanel;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.awt.*;

public class MainWindow {

    private JFrame frame;

    public MainWindow(DependencyProvider dependencyProvider) {
        initializeFrame();
        frame.setJMenuBar(createMenuBar());

        var templateModel = new TemplateModel(dependencyProvider.getTemplateCrudService());
        var categoryListModel = new CategoryListModel(dependencyProvider.getCategoryCrudService());
        var categoryModel = new CategoryModel(dependencyProvider.getCategoryCrudService(), categoryListModel);
        var currencyListModel = new CurrencyListModel(dependencyProvider.getCurrencyCrudService());
        var currencyModel = new CurrencyModel(dependencyProvider.getCurrencyCrudService(), currencyListModel);
        var carRideModel = new CarRidesModel(dependencyProvider.getRideCrudService(), categoryModel);

        var carRidesPanel = createCarRidesPanel(carRideModel, categoryListModel, templateModel, categoryModel, currencyListModel);
        var categoriesPanel = createCategoriesPanel(categoryModel);
        var templatesPanel = createTemplatesPanel(templateModel, categoryListModel, currencyListModel);
        var currencyPanel = createCurrencyPanel(currencyModel);

        var tabbedPane = createTabbedPane(carRidesPanel, categoriesPanel, templatesPanel, currencyPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.pack();
    }

    private void initializeFrame() {
        frame = new JFrame("Share Car Rider");
        frame.setSize(1024, 1024);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Menu");
        JMenuItem darkModeToggle = new JCheckBoxMenuItem(new DarkModeToggle(frame));
        fileMenu.add(darkModeToggle);
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

    private TemplatesPanel createTemplatesPanel(TemplateModel templateModel, CategoryListModel categoryListModel, CurrencyListModel currencyListModel) {
        return new TemplatesPanel(templateModel, categoryListModel, currencyListModel, this::changeActionsState);
    }

    private CurrencyPanel createCurrencyPanel(CurrencyModel currencyModel) {
        return new CurrencyPanel( currencyModel, this::changeActionsState);
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

    private void changeActionsState(int selectedItemsCount) {
    }
}
