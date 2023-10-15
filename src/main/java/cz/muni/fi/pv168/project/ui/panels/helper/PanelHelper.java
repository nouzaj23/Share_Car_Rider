package cz.muni.fi.pv168.project.ui.panels.helper;

import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.actions.DeleteAction;
import cz.muni.fi.pv168.project.ui.actions.EditAction;
import cz.muni.fi.pv168.project.ui.actions.QuitAction;
import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;

import javax.swing.*;
import java.awt.*;

public final class PanelHelper {

    public PanelHelper() {
        throw new AssertionError("This class is not instantiable");
    }

    public static void createTopBar(AbstractPanel panel, JTable table, JPanel filterPanel, JComponent extra) {
        var toolbar = new JToolBar();

        Action quitAction = new QuitAction<>(panel);
        Action addAction = new AddAction<>(panel);
        Action deleteAction = new DeleteAction<>(panel);
        Action editAction = new EditAction<>(panel);
        toolbar.add(new JButton(quitAction));
        toolbar.add(new JButton(addAction));
        toolbar.add(new JButton(deleteAction));
        toolbar.add(new JButton(editAction));

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
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
