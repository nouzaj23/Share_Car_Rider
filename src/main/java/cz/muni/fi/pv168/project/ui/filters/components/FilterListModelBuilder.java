package cz.muni.fi.pv168.project.ui.filters.components;

import cz.muni.fi.pv168.project.ui.model.CustomValuesModelDecorator;
import cz.muni.fi.pv168.project.ui.renderers.AbstractRenderer;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

public class FilterListModelBuilder<R> {
    private final ListModel<R> values;
    private AbstractRenderer<R> valuesRenderer;
    private Consumer<List<R>> filter;
    private int selectedIndex = 0;
    private int visibleRowsCount = 3;

    private FilterListModelBuilder(ListModel<R> values) {
        this.values = values;
    }

    public static <R> FilterListModelBuilder<R> create(ListModel<R> values) {
        return new FilterListModelBuilder<>(values);
    }

    public JList<R> build() {
        var jList = new JList<>(CustomValuesModelDecorator.addCustomValues(values));
        jList.setCellRenderer(valuesRenderer);
        jList.setSelectedIndex(Integer.MAX_VALUE);
        jList.setVisibleRowCount(visibleRowsCount);
        jList.addListSelectionListener(e -> filter.accept(jList.getSelectedValuesList()));

        return jList;
    }

    public FilterListModelBuilder<R> setValuesRenderer(AbstractRenderer<R> valuesRenderer)
    {
        this.valuesRenderer = valuesRenderer;
        return this;
    }

    public FilterListModelBuilder<R> setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
        return this;
    }

    public FilterListModelBuilder< R> setVisibleRowsCount(int visibleRowsCount)
    {
        this.visibleRowsCount = visibleRowsCount;
        return this;
    }

    public FilterListModelBuilder<R> setFilter(Consumer<List<R>> filter)
    {
        this.filter = filter;
        return this;
    }

    public Consumer<List<R>> getFilter() {
        return this.filter;
    }
}
