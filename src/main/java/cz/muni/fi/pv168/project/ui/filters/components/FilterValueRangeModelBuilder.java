package cz.muni.fi.pv168.project.ui.filters.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.function.Consumer;

public class FilterValueRangeModelBuilder<R> {
    private Consumer<String> filter;

    private FilterValueRangeModelBuilder() {
    }

    public static <R> FilterValueRangeModelBuilder<R> create() {
        return new FilterValueRangeModelBuilder<>();
    }

    public FilterValueRangeModelBuilder<R> setFilter(Consumer<String> filter) {
        this.filter = filter;
        return this;
    }

    public JTextField build() {
        var jNumberField = new JTextField(10);
        ((AbstractDocument) jNumberField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        jNumberField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                filter.accept(jNumberField.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                filter.accept(jNumberField.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                filter.accept(jNumberField.getText());
            }
        });

        return jNumberField;
    }
}
