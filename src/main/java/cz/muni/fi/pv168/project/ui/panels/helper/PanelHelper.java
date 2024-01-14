package cz.muni.fi.pv168.project.ui.panels.helper;

import cz.muni.fi.pv168.project.ui.actions.QuitAction;
import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;

import javax.swing.*;
import java.awt.*;

import java.util.List;

import java.awt.event.MouseListener;
import java.util.Optional;

public final class PanelHelper {

    public PanelHelper() {
        throw new AssertionError("This class is not instantiable");
    }

    public static void createTopBar(AbstractPanel panel, JTable table, JPanel filterPanel, JComponent extra, Optional<MouseListener> listener, List<Action> actions) {
        var toolbar = new JToolBar();

        Action quitAction = new QuitAction<>(panel);
        
        for (var action : actions) {
            toolbar.add(new JButton(action));
        }
        toolbar.add(new JButton(quitAction));

        if (extra != null) {
            toolbar.add(Box.createHorizontalGlue());
            toolbar.add(extra);
        } else {
            toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        }

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        topPanel.add(toolbar);

        if (filterPanel != null) {
            filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(filterPanel);
        }

        panel.add(topPanel, BorderLayout.NORTH);

        var scrollPane = new JScrollPane(table);
        if (listener != null && listener.isPresent()) {
            scrollPane.addMouseListener(listener.get());
        };
        panel.add(scrollPane, BorderLayout.CENTER);
    }
}
