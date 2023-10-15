package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditAction<E> extends AbstractAction {

    private final AbstractPanel<E> panel;

    public EditAction(AbstractPanel<E> panel) {
        super("Edit", Icons.EDIT_ICON);
        this.panel = panel;
        putValue(SHORT_DESCRIPTION, "Edit record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement logic
    }
}
