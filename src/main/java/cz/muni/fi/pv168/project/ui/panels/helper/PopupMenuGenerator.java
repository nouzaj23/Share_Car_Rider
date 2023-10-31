package cz.muni.fi.pv168.project.ui.panels.helper;

import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.actions.DeleteAction;
import cz.muni.fi.pv168.project.ui.actions.EditAction;
import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;

import javax.swing.*;

public final class PopupMenuGenerator {

    public static <E> JPopupMenu generatePopupMenu(AbstractPanel<E> panel) {
        var menu = new JPopupMenu();
        menu.add(new AddAction<>(panel));
        menu.add(new EditAction<>(panel));
        menu.add(new DeleteAction<>(panel));
        return menu;
    }
}
