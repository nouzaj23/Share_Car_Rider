package cz.muni.fi.pv168.project.ui.resources;

import javax.swing.*;
import java.net.URL;

public final class Icons {
    public static final Icon ADD_ICON = createIcon("/add.png");
    public static final Icon EDIT_ICON = createIcon("/edit.png");
    public static final Icon DELETE_ICON = createIcon("/delete.png");
    public static final Icon EXIT = createIcon("/exit.png");

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }


    private static ImageIcon createIcon(String name) {
        URL url = Icons.class.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Icon resource not found on classpath: " + name);
        }
        return new ImageIcon(url);
    }
}
