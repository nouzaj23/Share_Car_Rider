package cz.muni.fi.pv168.project.ui.editors;

import cz.muni.fi.pv168.project.business.model.Category;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionListener;

public class CategoryComboBoxEditor implements ComboBoxEditor {
    private JTextField editor;
    private Category category;

    public CategoryComboBoxEditor() {
        editor = new JTextField();
        editor.setEditable(true);
    }
    @Override
    public Component getEditorComponent() {
        return editor;
    }

    @Override
    public void setItem(Object anObject) {
        if (anObject instanceof Category) {
            category = (Category) anObject;
            editor.setText(category.getName());
        }
    }

    @Override
    public Object getItem() {
        return category;
    }

    @Override
    public void selectAll() {
        editor.selectAll();
    }

    @Override
    public void addActionListener(ActionListener l) {
        editor.addActionListener(l);
    }

    @Override
    public void removeActionListener(ActionListener l) {
        editor.removeActionListener(l);
    }
}
