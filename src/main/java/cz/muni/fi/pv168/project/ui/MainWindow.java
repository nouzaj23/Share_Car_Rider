package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;
import cz.muni.fi.pv168.project.ui.panels.CategoriesPanel;
import cz.muni.fi.pv168.project.ui.panels.TemplatesPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow {

    private JFrame frame;

    public MainWindow() {
        initFrame();
        frame.setJMenuBar(createMenuBar());

        var tabbedPane = new JTabbedPane();
        var carRidesPanel = new CarRidesPanel();
        var categoriesPanel = new CategoriesPanel();
        var templatesPanel = new TemplatesPanel();
        tabbedPane.addTab("Car Rides", carRidesPanel);
        tabbedPane.addTab("Categories", categoriesPanel);
        tabbedPane.addTab("Templates", templatesPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.pack();
    }

    private void initFrame() {
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
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    public void show() {
        frame.setVisible(true);
    }
}
