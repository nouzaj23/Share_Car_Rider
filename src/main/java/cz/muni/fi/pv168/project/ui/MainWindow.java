package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.TestDataGenerator;
import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.actions.DarkModeToggle;
import cz.muni.fi.pv168.project.ui.actions.DeleteAction;
import cz.muni.fi.pv168.project.ui.actions.EditAction;
import cz.muni.fi.pv168.project.ui.model.CarRidesModel;
import cz.muni.fi.pv168.project.ui.model.CategoryListModel;
import cz.muni.fi.pv168.project.ui.model.CategoryModel;
import cz.muni.fi.pv168.project.ui.model.TemplateModel;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;
import cz.muni.fi.pv168.project.ui.panels.CategoriesPanel;
import cz.muni.fi.pv168.project.ui.panels.TemplatesPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainWindow {

    private JFrame frame;

    public MainWindow() {
        initializeFrame();
        frame.setJMenuBar(createMenuBar());

        var carRideModel = new CarRidesModel(new ArrayList<>());
        var templateModel = new TemplateModel(new ArrayList<>());
        var categoryListModel = new CategoryListModel(TestDataGenerator.CATEGORIES);
        var categoryModel = new CategoryModel(categoryListModel);

        var carRidesPanel = createCarRidesPanel(carRideModel, categoryListModel, templateModel);
        var categoriesPanel = createCategoriesPanel(categoryModel);
        var templatesPanel = createTemplatesPanel(templateModel, categoryListModel);

        var tabbedPane = createTabbedPane(carRidesPanel, categoriesPanel, templatesPanel);

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
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenuItem darkModeToggle = new JCheckBoxMenuItem(new DarkModeToggle(frame));
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        fileMenu.add(darkModeToggle);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private CarRidesPanel createCarRidesPanel(CarRidesModel carRideModel, CategoryListModel categoryListModel, TemplateModel templateModel) {
        var carRidesPanel = new CarRidesPanel(carRideModel, categoryListModel, this::changeActionsState, templateModel);
        carRidesPanel.setComponentPopupMenu(createRidesPopupMenu(carRidesPanel));
        return carRidesPanel;
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

    private JPopupMenu createRidesPopupMenu(CarRidesPanel carRidesPanel) {
        var menu = new JPopupMenu();
        menu.add(new AddAction<>(carRidesPanel));
        menu.add(new EditAction<>(carRidesPanel));
        menu.add(new DeleteAction<>(carRidesPanel));
        return menu;
    }

    public void show() {
        frame.setVisible(true);
    }

    private void changeActionsState(int selectedItemsCount) {
    }
}
