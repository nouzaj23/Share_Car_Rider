package cz.muni.fi.pv168.project.ui.model;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * This class decorates existing {@link ListModel} or {@link ComboBoxModel} to contain also
 * custom {@link Enum} values at the start of the model values.
 */
public final class CustomValuesModelDecorator {

    private CustomValuesModelDecorator() {
    }

    public static <E> ListModel<E> addCustomValues(ListModel<E> decoratedModel) {
        return new ListModelDecorator<>(decoratedModel);
    }

    private static class ListModelDecorator<E, M extends ListModel<E>> implements ListModel<E> {
        private final M decoratedModel;
        private final Map<ListDataListener, TransposingListener> listeners = new IdentityHashMap<>();

        private ListModelDecorator(M decoratedModel) {
            this.decoratedModel = decoratedModel;
        }

        @Override
        public int getSize() {
            return decoratedModel.getSize();
        }

        @Override
        public E getElementAt(int index) {
            return decoratedModel.getElementAt(index);
        }

        @Override
        public void addListDataListener(ListDataListener listener) {
            var transposingListener = new TransposingListener(listener);
            listeners.put(listener, transposingListener);
            decoratedModel.addListDataListener(transposingListener);
        }

        @Override
        public void removeListDataListener(ListDataListener listener) {
            TransposingListener transposingListener = listeners.remove(listener);
            if (transposingListener != null) {
                decoratedModel.removeListDataListener(transposingListener);
            }
        }

    }

    private static class TransposingListener implements ListDataListener {

        private final ListDataListener delegate;

        private TransposingListener(ListDataListener delegate) {
            this.delegate = delegate;
        }

        @Override
        public void intervalAdded(ListDataEvent event) {
            delegate.intervalAdded(transposeIndexes(event));
        }

        @Override
        public void intervalRemoved(ListDataEvent event) {
            delegate.intervalRemoved(transposeIndexes(event));
        }

        @Override
        public void contentsChanged(ListDataEvent event) {
            delegate.contentsChanged(transposeIndexes(event));
        }

        private ListDataEvent transposeIndexes(ListDataEvent event) {
            return new ListDataEvent(
                    event.getSource(),
                    event.getType(),
                    transposeIndex(event.getIndex0()),
                    transposeIndex(event.getIndex1())
            );
        }

        private int transposeIndex(int index) {
            return index;
        }
    }
}
