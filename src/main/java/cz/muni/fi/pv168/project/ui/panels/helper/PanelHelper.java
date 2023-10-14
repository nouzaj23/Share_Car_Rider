package cz.muni.fi.pv168.project.ui.panels.helper;

import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;

import javax.swing.*;
import java.awt.*;

public final class PanelHelper {

    public PanelHelper() {
        throw new AssertionError("This class is not instantiable");
    }

    public static void createTopBar(AbstractPanel panel, JTable table, JPanel filterPanel) {
        var toolbar = new JToolBar();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        Action addAction = new AddAction<>(panel);
        toolbar.add(new JButton(addAction));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        topPanel.add(toolbar);
        if (filterPanel != null) {
            topPanel.add(filterPanel);
        }

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
