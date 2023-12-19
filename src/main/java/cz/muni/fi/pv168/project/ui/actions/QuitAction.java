package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class QuitAction<E> extends AbstractAction {

    private final AbstractPanel<E> panel;

    public QuitAction(AbstractPanel<E> panel) {
        super("Quit", Icons.EXIT);
        this.panel = panel;
        putValue(SHORT_DESCRIPTION, "Close the application");
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Q"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var answer = JOptionPane.showConfirmDialog(panel,
                "Are you sure you want to quit the app?",
                "Quit the app",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
