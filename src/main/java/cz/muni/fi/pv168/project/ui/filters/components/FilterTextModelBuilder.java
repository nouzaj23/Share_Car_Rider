package cz.muni.fi.pv168.project.ui.filters.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

public class FilterTextModelBuilder<R> {
    private Consumer<String> filter;

    private FilterTextModelBuilder() {
    }

    public static <R> FilterTextModelBuilder<R> create() {
        return new FilterTextModelBuilder<>();
    }

    public FilterTextModelBuilder<R> setFilter(Consumer<String> filter) {
        this.filter = filter;
        return this;
    }

    public JTextField build() {
        var jText = new JTextField(10);
        jText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                filter.accept(jText.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                filter.accept(jText.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                filter.accept(jText.getText());
            }
        });

        return jText;
    }
}
