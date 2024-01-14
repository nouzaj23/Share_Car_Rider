package cz.muni.fi.pv168.project.ui.panels.helper;

import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;

import java.util.List;

import javax.swing.*;

public final class PopupMenuGenerator {

    public static <E> JPopupMenu generatePopupMenu(AbstractPanel<E> panel, List<Action> actions) {
        var menu = new JPopupMenu();
        for (var action : actions) {
            menu.add(action);
        }
        return menu;
    }
}
