package cz.muni.fi.pv168.project.ui.dialog;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Optional;

import static javax.swing.JOptionPane.*;

abstract public class EntityDialog<E> {

    private final JPanel panel = new JPanel();
    private final JPanel buttonPanel = new JPanel();

    EntityDialog() {
        panel.setLayout(new MigLayout("wrap 2"));
    }

    void addButton(JButton button) {
        buttonPanel.add(button);
    }

    void add(String labelText, JComponent component) {
        var label = new JLabel(labelText);
        panel.add(label);
        panel.add(component, "wmin 250lp, grow");
    }

    abstract E getEntity();

    public Optional<E> show(JComponent parentComponent, String title) {
        panel.add(buttonPanel, "span 2, align center");
        int result = JOptionPane.showOptionDialog(parentComponent, panel, title,
                OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
        if (result == OK_OPTION) {
            return Optional.of(getEntity());
        }
        return Optional.empty();
    }
}
