package org.valkyriercp.list;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.TransformingComparator;
import org.springframework.util.Assert;
import org.springframework.util.comparator.ComparableComparator;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Mathias Broekelmann
 */
public class SortedListModel extends AbstractFilteredListModel {

    private static Comparator comparableComparator = new ComparableComparator();

    private Comparator comparator;

    private Integer[] indexes;

    private final Observer comparatorObserver = new ComparatorObserver();

    public SortedListModel(ListModel model) {
        this(model, comparableComparator);
    }

    public SortedListModel(ListModel model, Comparator comparator) {
        super(model);
        Assert.notNull(comparator);
        this.comparator = comparator;
        if (comparator instanceof Observable) {
            ((Observable) comparator).addObserver(comparatorObserver);
        }
        reallocateIndexes();
    }

    public void setComparator(Comparator comparator) {
        Assert.notNull(comparator);
        if (this.comparator instanceof Observable) {
            ((Observable) comparator).deleteObserver(comparatorObserver);
        }
        this.comparator = comparator;
        if (this.comparator instanceof Observable) {
            ((Observable) comparator).addObserver(comparatorObserver);
        }
        applyComparator();
    }

    /**
     * Internally called to reallocate the indexes. This method should be called when the filtered model changes its
     * element size
     */
    protected void reallocateIndexes() {
        indexes = new Integer[getFilteredModel().getSize()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = new Integer(i);
        }
        applyComparator();
    }

    /**
     * Returns the element index for a sorted index
     *
     * @param sortedIndex
     *            the sorted index
     * @return the unsorted index of the filtered model
     */
    public int getElementIndex(int sortedIndex) {
        return indexes[sortedIndex].intValue();
    }

    protected void applyComparator() {
        Integer[] indexes = new Integer[this.indexes.length];
        System.arraycopy(this.indexes, 0, indexes, 0, indexes.length);
        Arrays.sort(indexes, new TransformingComparator(new IndexToElementTransformer(getFilteredModel()), comparator));
        this.indexes = indexes;
        fireContentsChanged(this, -1, -1);
    }

    public void contentsChanged(ListDataEvent e) {
        reallocateIndexes();
    }

    public void intervalAdded(ListDataEvent e) {
        reallocateIndexes();
    }

    public void intervalRemoved(ListDataEvent e) {
        reallocateIndexes();
    }

    private static class IndexToElementTransformer implements Transformer {
        private final ListModel model;

        public IndexToElementTransformer(ListModel model) {
            this.model = model;
        }

        public Object transform(Object input) {
            return model.getElementAt(((Integer) input).intValue());
        }

    }

    private class ComparatorObserver implements Observer {
        public void update(Observable o, Object arg) {
            applyComparator();
        }
    }
}
