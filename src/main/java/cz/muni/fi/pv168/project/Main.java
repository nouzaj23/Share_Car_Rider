package cz.muni.fi.pv168.project;

import com.formdev.flatlaf.FlatLightLaf;
import cz.muni.fi.pv168.project.ui.MainWindow;
import cz.muni.fi.pv168.project.wiring.ProductionDependencyProvider;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        initFlatLightLafLookAndFeel();

        //initNimbusLookAndFeel();
        EventQueue.invokeLater(() -> new MainWindow(new ProductionDependencyProvider()).show());
    }

    private static void initFlatLightLafLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Failed to initialize LaF", ex);
        }
    }
}


