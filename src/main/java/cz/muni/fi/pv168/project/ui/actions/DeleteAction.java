package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class DeleteAction<E> extends AbstractAction {

    private final AbstractPanel<E> panel;

    public DeleteAction(AbstractPanel<E> panel) {
        super("Delete", Icons.DELETE_ICON);
        this.panel = panel;
        putValue(SHORT_DESCRIPTION, "Delete record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement logic
    }
}
