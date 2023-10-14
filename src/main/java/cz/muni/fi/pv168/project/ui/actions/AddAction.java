package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddAction<E> extends AbstractAction {

    private final AbstractPanel<E> panel;

    public AddAction(AbstractPanel<E> panel) {
        super("Add", Icons.ADD_ICON);
        this.panel = panel;
        putValue(SHORT_DESCRIPTION, "Adds record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = panel.getDialog();
        dialog.show(panel, "Add record")
                .ifPresent(panel::addRow);
    }
}
