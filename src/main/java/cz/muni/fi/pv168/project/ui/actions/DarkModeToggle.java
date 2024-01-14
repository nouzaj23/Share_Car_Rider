package cz.muni.fi.pv168.project.ui.actions;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import cz.muni.fi.pv168.project.Main;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Adam Paulen
 */
public class DarkModeToggle extends AbstractAction {

    private boolean isDark = false;
    private final JFrame frame;

    public DarkModeToggle(JFrame frame){
        super("DarkMode");
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isDark){
            initFlatLightLafLookAndFeel();
        } else {
            initFlatDarkLafLookAndFeel();
        }
        isDark = !isDark;
        SwingUtilities.updateComponentTreeUI(SwingUtilities.getRoot(frame));
    }

    private static void initFlatLightLafLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            CarRidesPanel.changeStatsColorLight();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Failed to initialize LaF", ex);
        }
    }

    private static void initFlatDarkLafLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            CarRidesPanel.changeStatsColorDark();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Failed to initialize LaF", ex);
        }
    }
}
